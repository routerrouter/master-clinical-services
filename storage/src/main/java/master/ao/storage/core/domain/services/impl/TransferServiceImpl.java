package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.enums.TransferType;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.exceptions.ExistingDataException;
import master.ao.storage.core.domain.exceptions.UserNotFoundException;
import master.ao.storage.core.domain.models.*;
import master.ao.storage.core.domain.repositories.StockRepository;
import master.ao.storage.core.domain.repositories.TransferRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.LocationService;
import master.ao.storage.core.domain.services.ProductService;
import master.ao.storage.core.domain.services.StorageService;
import master.ao.storage.core.domain.services.TransferService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository repository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final ProductService productService;
    private final LocationService locationService;
    private final StorageService storageService;


    @Override
    public Transfer saveTransfer(Transfer transfer, UUID userId) {

        Transfer transferBeforeSaved = new Transfer();
        BeanUtils.copyProperties(transfer, transferBeforeSaved);

        Transfer transferAfterSaved = new Transfer();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        var storage = storageService.fetchOrFail(transfer.getStorage().getStorageId()).get();

        transfer.setUserGroup(user.getGroupId());
        transfer.setStorage(storage);
        transfer = saveTransferOut(transfer, user);

        saveTransferIn(transfer, user);

        return transfer;
    }

    private Transfer saveTransferOut(Transfer transfer, User user) {
        transfer.setUserId(user.getUserId());
        transfer.setTransferType(TransferType.OUTPUT);

        validateItemsOut(transfer);

        return save(transfer);
    }

    private Transfer saveTransferIn(Transfer transfer, User user) {
        var transferInput = new Transfer();
        transferInput.setTransferType(TransferType.INPUT);
        transferInput.setUserId(user.getUserId());
        transferInput.setUserGroup(user.getGroupId());
        transferInput.setDescription(String.format("Recebimento de produtos via transferência pelo armazem: %s", transfer.getStorage().getStorageId().toString()));
        transferInput.setTransferDate(LocalDate.now());
        transferInput.setStorage(transfer.getItems().stream().findFirst().get().getLocation().getStorage());

        Set<ItemsTransfer> itemsList = new HashSet<>();
        transfer.getItems().forEach(items -> items.setItemTransferId(null));
        transfer.getItems().forEach(items -> items.getTransfer().setTransferId(null));
        itemsList.addAll(transfer.getItems());
        transferInput.setItems(itemsList);
        validateItemsIn(transferInput);

        return save(transferInput);
    }

    public Transfer save(Transfer transfer) {
        return repository.save(transfer);
    }

    private void validateItemsIn(Transfer transfer) {

        transfer.getItems()
                .forEach(item -> {

                    Product product = productService.fetchOrFail(item.getProduct().getProductId()).get();
                    Location location = locationService.fetchOrFail(item.getLocation().getLocationId()).get();

                    item.setTransfer(transfer);
                    item.setProduct(product);
                    item.setLocation(location);

                    if (isEquipment(item.getProduct())) {
                        validateEquipmentStock(transfer.getStorage().getStorageId(), item);
                    } else {
                        validateProductStock(transfer.getStorage().getStorageId(), item);
                    }
                });
    }

    private void validateProductStock(UUID storageOriginId, ItemsTransfer item) {
        var productStockOriginInfoOptional = stockRepository.findIsProductStocked(storageOriginId,
                item.getProduct().getProductId(), item.getExpirationDate(), item.getLote()).get();

        var productStockOptional = stockRepository.findIsProductStocked(getStorageByLocation(item.getLocation()),
                item.getProduct().getProductId(), item.getExpirationDate(), item.getLote());

        Location locationOptional = locationService.fetchOrFail(item.getLocation().getLocationId()).get();
        item.setLocation(locationOptional);

        if (productStockOptional.isPresent()) {
            productStockOptional.get().setQuantity(productStockOptional.get().getQuantity() + item.getQuantity());
            updateProductStock(productStockOptional.get());
        } else {
            updateProductStock(setStock(item, productStockOriginInfoOptional.getCust()));
        }
    }

    private void validateEquipmentStock(UUID storageOriginId, ItemsTransfer item) {
        var equipmentStockOriginInfoOptional = stockRepository.findIsEquipmentStocked(storageOriginId,
                item.getProduct().getProductId(), item.getLifespan(), item.getModel()).get();

        var equipmentStockOptional = stockRepository.findIsEquipmentStocked(getStorageByLocation(item.getLocation()),
                item.getProduct().getProductId(), item.getLifespan(), item.getModel());

        var locationOptional = locationService.fetchOrFail(item.getLocation().getLocationId()).get();
        item.setLocation(locationOptional);

        if (equipmentStockOptional.isPresent()) {
            equipmentStockOptional.get().setQuantity(equipmentStockOptional.get().getQuantity() + item.getQuantity());
            updateProductStock(equipmentStockOptional.get());
        } else {
            updateProductStock(setStock(item, equipmentStockOriginInfoOptional.getCust()));
        }
    }


    private void validateItemsOut(Transfer transfer) {
        transfer.getItems()
                .forEach(item -> {

                    Product product = productService.fetchOrFail(item.getProduct().getProductId()).get();
                    Location location = locationService.fetchOrFail(item.getLocation().getLocationId()).get();

                    item.setTransfer(transfer);
                    item.setProduct(product);
                    item.setLocation(location);

                    if (isEquipment(item.getProduct())) {
                        var equipmentStockOptional = stockRepository.findIsEquipmentStocked(transfer.getStorage().getStorageId(),
                                item.getProduct().getProductId(), item.getLifespan(), item.getModel());

                        if (equipmentStockOptional.isPresent()) {
                            if (item.getQuantity() < equipmentStockOptional.get().getQuantity()) {
                                equipmentStockOptional.get().setQuantity(equipmentStockOptional.get().getQuantity() - item.getQuantity());
                                updateProductStock(equipmentStockOptional.get());
                            } else {
                                throw new BussinessException("Quantidade de transferencia não deve ser maior que a existente!");
                            }

                        }
                    } else {
                        var productStockOptional = stockRepository.findIsProductStocked(transfer.getStorage().getStorageId(),
                                item.getProduct().getProductId(), item.getExpirationDate(), item.getLote());

                        if (productStockOptional.isPresent()) {
                            if (item.getQuantity() < productStockOptional.get().getQuantity()) {
                                productStockOptional.get().setQuantity(productStockOptional.get().getQuantity() - item.getQuantity());
                                updateProductStock(productStockOptional.get());
                            } else {
                                throw new BussinessException("Quantidade de transferencia não deve ser maior que a existente!");
                            }

                        }
                    }
                });
    }

    private boolean isEquipment(Product request) {
        var product = productService.fetchOrFail(request.getProductId()).get();
        return product.getGroup().getName().contains("EQUIPAMENTO");
    }

    private UUID getStorageByLocation(Location location) {
        return locationService.fetchOrFail(location.getLocationId())
                .get()
                .getStorage()
                .getStorageId();
    }


    private void updateProductStock(Stock stock) {
        stockRepository.save(stock);
    }

    private Stock setStock(ItemsTransfer items, BigDecimal cust) {
        var stock = new Stock();
        stock.setStorage(items.getLocation().getStorage());
        stock.setLote(items.getLote());
        stock.setProduct(items.getProduct());
        stock.setQuantity(items.getQuantity());
        stock.setExpirationDate(items.getExpirationDate());
        stock.setLocation(items.getLocation());
        stock.setCust(cust);
        stock.setAcquisitionDate(items.getAcquisitionDate());
        stock.setManufactureDate(items.getManufactureDate());
        stock.setUnitType(items.getUnitType());

        return stock;
    }


    @Override
    public List<Transfer> listByStorage(UUID storageId) {
        return repository.findAll(storageId);
    }
}

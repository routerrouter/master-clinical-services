package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.config.security.AuthenticationCurrentUserService;
import master.ao.storage.core.domain.enums.TransferType;
import master.ao.storage.core.domain.exceptions.*;
import master.ao.storage.core.domain.models.*;
import master.ao.storage.core.domain.repositories.*;
import master.ao.storage.core.domain.services.*;
import master.ao.storage.core.domain.utils.Converts;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository repository;
    private final ItemsTransferRepository itemsTransferRepository;
    private final StockRepository stockRepository;
    private final StockService stockService;
    private final ProductService productService;
    private final LocationService locationService;
    private final StorageService storageService;
    private final UtilService utilService;
    private final AuthenticationCurrentUserService currentUserService;

    private String storageToSend = "";

    @Override
    public Transfer saveTransfer(Transfer transfer, UUID destineStorageId) {

        Transfer transferBeforeSaved = new Transfer();
        BeanUtils.copyProperties(transfer, transferBeforeSaved);

        var storage = storageService.fetchOrFail(transfer.getStorage().getStorageId()).get();

        transfer.setUserGroup(utilService.getUserGroup());
        transfer.setStorage(storage);

        var destinationStorage =  storageService.fetchOrFail(destineStorageId);

        transfer.setSendDescription(destinationStorage.get().getDescription());
        transfer = saveTransferOut(transfer);

        saveTransferIn(transfer,destineStorageId);

        return transfer;
    }

    private Transfer saveTransferOut(Transfer transfer) {
        transfer.setUserId(currentUserService.getCurrentUser().getUserId());
        transfer.setTransferType(TransferType.OUTPUT);

        validateItemsOut(transfer);

        return save(transfer);
    }

    private Transfer saveTransferIn(Transfer transfer, UUID storageDestineId) {
        var transferInput = new Transfer();
        transferInput.setTransferType(TransferType.INPUT);
        transferInput.setUserId(currentUserService.getCurrentUser().getUserId());
        transferInput.setUserGroup(utilService.getUserGroup());
        transferInput.setTransferDate(LocalDate.now());

        var storage = storageService.fetchOrFail(storageDestineId);

        transferInput.setStorage(storage.get());
        transfer.setReceiveDescription();
        transferInput.setDescription(transfer.getDescription());
        Set<ItemsTransfer> itemsList = new HashSet<>();
        transfer.getItems().forEach(items -> items.setItemTransferId(null));
        transfer.getItems().forEach(items -> items.getTransfer().setTransferId(null));
        itemsList.addAll(transfer.getItems());
        transferInput.setItems(itemsList);
        validateItemsIn(transfer.getStorage().getStorageId(),transferInput);

        return save(transferInput);
    }

    public Transfer save(Transfer transfer) {
        return repository.save(transfer);
    }

    private void validateItemsIn(UUID originStorageId, Transfer transfer) {

        transfer.getItems()
                .forEach(item -> {

                    var product = productService.fetchOrFail(item.getProduct().getProductId()).get();
                    var location = locationService.fetchOrFail(item.getLocation().getLocationId()).get();
                    var storage = storageService.fetchOrFail(originStorageId).get();

                    item.setTransfer(transfer);
                    item.setProduct(product);
                    item.setLocation(location);

                    Stock stock = getStock(item,storage);

                    item.setAcquisitionDate(stock.getAcquisitionDate());
                    item.setManufactureDate(stock.getManufactureDate());

                    BeanUtils.copyProperties(item, stock);
                    stock.setStorage(transfer.getStorage());

                    var optionalExistence = stockService.fetchExistence(stock);

                    if (optionalExistence.isPresent()) {
                        optionalExistence.get().setQuantity(optionalExistence.get().getQuantity() + item.getQuantity());
                        updateOrSaveProductStock(optionalExistence.get());
                    } else {
                        stock.setStockId(null);
                        updateOrSaveProductStock(stock);
                    }

                });
    }



    private void validateItemsOut(Transfer transfer) {
        transfer.getItems()
                .forEach(item -> {

                    Product product = productService.fetchOrFail(item.getProduct().getProductId()).get();
                    Location location = locationService.fetchOrFail(item.getLocation().getLocationId()).get();

                    item.setTransfer(transfer);
                    item.setProduct(product);
                    item.setLocation(location);


                    Stock stock = new Stock();
                    BeanUtils.copyProperties(item, stock);
                    stock.setStorage(transfer.getStorage());

                    var optionalExistence = stockService.fetchOrFailExistence(stock);

                    if (item.getQuantity() < optionalExistence.get().getQuantity()) {
                        optionalExistence.get().setQuantity(optionalExistence.get().getQuantity() - item.getQuantity());
                        updateOrSaveProductStock(optionalExistence.get());
                    } else {
                        log.debug("Produto: {} ", item.getProduct());
                        log.info("Quantidade de transferencia não deve ser maior que a existente!");
                    }

                });
    }


    private UUID getStorageByLocation(Location location) {
        return locationService.fetchOrFail(location.getLocationId())
                .get()
                .getStorage()
                .getStorageId();
    }


    private void updateOrSaveProductStock(Stock stock) {
        stockRepository.save(stock);
    }

    private Stock getStock(ItemsTransfer item, Storage storage) {

        var stock = new Stock();
        stock.setStorage(storage);
        BeanUtils.copyProperties(item, stock);

        var stockOptional = stockService.fetchOrFailExistence(stock);

        return stockOptional.get();
    }


    @Override
    public List<Transfer> listByStorage(UUID storageId) {
        return repository.findAll(storageId);
    }

    @Override
    public List<ItemsTransfer> listItemsByTransfer(UUID transferId) {
        fetchOrFail(transferId);

        return itemsTransferRepository.findAllByTransferId(transferId)
                .stream()
                .collect(Collectors.toList());
    }


    public Optional<Transfer> fetchOrFail(UUID transferId) {
        var transfer = repository.findById(transferId)
                .orElseThrow(() -> new TransferNotFoundException(transferId));

        return Optional.of(transfer);
    }

}

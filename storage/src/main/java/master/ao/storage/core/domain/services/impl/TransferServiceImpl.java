package master.ao.storage.core.domain.services.impl;

import master.ao.storage.core.domain.exceptions.UserNotFoundException;
import master.ao.storage.core.domain.models.ItemsTransfer;
import master.ao.storage.core.domain.models.Product;
import master.ao.storage.core.domain.models.Stock;
import master.ao.storage.core.domain.models.Transfer;
import master.ao.storage.core.domain.repositories.StockRepository;
import master.ao.storage.core.domain.repositories.TransferRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.TransferService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository repository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    public TransferServiceImpl(TransferRepository repository, UserRepository userRepository, StockRepository stockRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public Transfer saveTransfer(Transfer transfer, UUID originStorageId, UUID userId) {

        validateTransfer(transfer);
        validateItems(transfer);

        var user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));

        transfer.setUserGroup(user.getGroupId());
        return repository.save(transfer);
    }

    private void validateItems(Transfer transfer) {

        transfer.getItems()
                .forEach(item -> {
                    setStockInDestine(item);

                    if (isEquipment(item.getProduct())) {
                        var equipmentStockOptional = stockRepository.findIsEquipmentStocked(item.getLocation().getStorage().getStorageId(),
                                item.getProduct().getProductId(),item.getLifespan(), item.getModel());
                    } else {
                        var productStockOptional = stockRepository.findIsProductStocked(item.getLocation().getStorage().getStorageId(),
                                item.getProduct().getProductId(),item.getExpirationDate(),item.getLote());
                    }
                });
    }

    private void setStockInDestine(ItemsTransfer items) {
        var stock = new Stock();
        stock.setStorage(items.getLocation().getStorage());
        stock.setLote(items.getLote());
        stock.setProduct(items.getProduct());
        stock.setQuantity(items.getQuantity());
        stock.setExpirationDate(items.getExpirationDate());
        stock.setLocation(items.getLocation());
    }

    private void validateTransfer(Transfer transfer) {
    }

    @Override
    public List<Transfer> listAll() {
        return null;
    }

    private boolean isEquipment(Product product) {
        return product.getGroup().getName().contains("EQUIPAMENTO") ? true : false;
    }
}

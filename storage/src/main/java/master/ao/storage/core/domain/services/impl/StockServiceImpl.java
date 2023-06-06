package master.ao.storage.core.domain.services.impl;

import master.ao.storage.core.domain.enums.MovementType;
import master.ao.storage.core.domain.exceptions.StockNotFoundException;
import master.ao.storage.core.domain.models.Product;
import master.ao.storage.core.domain.models.Stock;
import master.ao.storage.core.domain.models.Storage;
import master.ao.storage.core.domain.repositories.StockRepository;
import master.ao.storage.core.domain.services.ProductService;
import master.ao.storage.core.domain.services.StockService;
import master.ao.storage.core.domain.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository repository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ProductService productService;

    LocalDate systemDate = LocalDate.now();

    @Transactional
    @Override
    public Stock saveOrUpdate(Stock stockRequest, MovementType movementType) {

        // Verificar o tipo do movimento enviado(INPUT/OUTPUT)
        // Verificar existencia do item pelo armazem informado
        // Efetuar a operação de acordo ao tipo do movimento


        Optional<Stock> stocked = null;
        Stock stock = new Stock();
        stock.setAcquisitionDate(stockRequest.getAcquisitionDate());

        if (isEquipment(stockRequest.getProduct())) {
            stocked = repository.existEquipmentOnStock(stockRequest);
            if (stocked.isPresent()) {
                stock = stocked.get();
                //stock.setLastUpdateAt(LocalDateTime.now(ZoneId.of("UTC")));
                stock.setModel(stockRequest.getModel());
                stock.setSerialNumber(stockRequest.getSerialNumber());
                stock.setLifespan(stockRequest.getLifespan());
                stock.setQuantity(movementType.quantityUpdated(stock.getQuantity(), stockRequest.getQuantity()));
            } else {
                stock = repository.save(stockRequest);
            }
        } else {
            stocked = repository.existProductOnStock(stockRequest);
            if (stocked.isPresent()) {
                stock = stocked.get();
               // stock.setLastUpdateAt(LocalDateTime.now(ZoneId.of("UTC")));
                stock.setLocation(stockRequest.getLocation());
                stock.setQuantity(movementType.quantityUpdated(stock.getQuantity(), stockRequest.getQuantity()));
                repository.updateStockExistence(stock);
            } else {
                stock = repository.save(stockRequest);
            }
        }

        return stock;
    }

    @Override
    public List<Stock> findByLocation(UUID locationId) {
        return null;
    }

    @Override
    public List<Stock> findCriticalProducts(UUID storageId) {
        storageService.fetchOrFail(storageId);
        List<Stock> criticalList = repository.findAll(storageId).stream()
                .filter(product -> product.getQuantity() <= product.getProduct().getCriticalAmount())
                .sorted(getProductComparator())
                .collect(Collectors.toList());

        return criticalList;
    }

    @Override
    public List<Stock> findExpiredProducts(UUID storageId) {
        storageService.fetchOrFail(storageId);
        List<Stock> expiredList = repository.findAll(storageId).stream()
                .filter(product -> (product.getExpirationDate()==null ? LocalDate.now() :product.getExpirationDate()).isBefore(systemDate))
                .sorted(getProductComparator())
                .collect(Collectors.toList());

        return expiredList;
    }

    @Override
    public List<Stock> findInventory(UUID storageId) {
        storageService.fetchOrFail(storageId);
        List<Stock> inventoryList = repository.findAll(storageId).stream()
                .filter(product -> product.getQuantity() > 0)
                .sorted(getProductComparator())
                .collect(Collectors.toList());
        return inventoryList;
    }

    @Override
    public void updateProductCost(Stock stock) {
        var storage = storageService.fetchOrFail(stock.getStorage().getStorageId());
        var product = productService.fetchOrFail(stock.getProduct().getProductId());
        if (isEquipment(product.get())) {
            var equipmentStockOptional = repository.findIsEquipmentStocked(storage.get().getStorageId(),
                    product.get().getProductId(),stock.getLifespan(), stock.getModel()).get();

            equipmentStockOptional.setCust(stock.getCust());
            repository.save(equipmentStockOptional);

        } else {
            var productStockOptional = repository.findIsProductStocked(storage.get().getStorageId(),
                    product.get().getProductId(),stock.getExpirationDate(), stock.getLote())
                    .orElseThrow(()-> new  StockNotFoundException());

            productStockOptional.setCust(stock.getCust());
            repository.save(productStockOptional);
        }

    }

    private boolean isEquipment(Product product) {
        return product.getGroup().getName().contains("EQUIPAMENTO") ? true : false;
    }

    private Comparator<Stock> getProductComparator() {
        return (stock1, stock2) -> stock1.getProduct().getName().
                compareTo(stock2.getProduct().getName());
    }

    private void validateStock(Stock stock) {
        storageService.fetchOrFail(stock.getStorage().getStorageId());
        productService.fetchOrFail(stock.getProduct().getProductId());
    }
}

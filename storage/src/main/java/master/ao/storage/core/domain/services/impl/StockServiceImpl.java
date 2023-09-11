package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.config.security.AuthenticationCurrentUserService;
import master.ao.storage.core.domain.enums.MovementType;
import master.ao.storage.core.domain.enums.UpdateStockType;
import master.ao.storage.core.domain.exceptions.StockNotFoundException;
import master.ao.storage.core.domain.models.LogStock;
import master.ao.storage.core.domain.models.Product;
import master.ao.storage.core.domain.models.Stock;
import master.ao.storage.core.domain.repositories.StockRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private  final StockRepository repository;
    private final StorageService storageService;
    private final ProductService productService;
    private final LocationService locationService;
    private final LogStockService logStockService;
    private final AuthenticationCurrentUserService currentUserService;
    private final UserService userService;

    private LocalDate systemDate = LocalDate.now();
    private String beforeValue = "0";
    private String alterValue = "0";

    @Transactional
    @Override
    public Stock saveOrUpdate(Stock stockRequest, MovementType movementType) {

        Stock stock = new Stock();
        stock.setAcquisitionDate(stockRequest.getAcquisitionDate());
        stockRequest.setStorage(stockRequest.getLocation().getStorage());

        var stockOptional = repository.findStock(stockRequest);

        if (stockOptional.isPresent()) {
            stockOptional.get().setQuantity(movementType.quantityUpdated(stockOptional.get().getQuantity(), stockRequest.getQuantity()));
            stockOptional.get().updateCostExistence(stockRequest.getCost());
            BeanUtils.copyProperties(stockOptional.get(), stock);
            repository.save(stock);
        } else {
            stock = repository.save(stockRequest);
        }

        return stock;
    }

    @Override
    public List<Stock> findByLocation(UUID locationId) {
        locationService.fetchOrFail(locationId);
        return repository.findAllProductStockByLocation(locationId);
    }

    @Override
    public List<Stock> findExistenceByProduct(UUID productId) {
        productService.fetchOrFail(productId);
        return repository.findStockExistenceByProduct(productId);
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
                .filter(product -> (product.getExpirationDate() == null ? LocalDate.now() :product.getExpirationDate()).isBefore(systemDate))
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
        validateStock(stock);

        var stockOptional = repository.findStock(stock)
                .orElseThrow(()-> new  StockNotFoundException());

        alterValue = String.valueOf(stock.getCost());
        beforeValue = String.valueOf(stockOptional.getCost());

        stockOptional.setCost(stock.getCost());

        var stockUpdated = repository.save(stockOptional);
        saveLogUpdate(UpdateStockType.UPDATE_COST,stockUpdated);
    }

    @Override
    public void updateProductExistence(Stock stock) {
        validateStock(stock);
        Stock existence = fetchOrFailExistence(stock).get();

        alterValue = String.valueOf(stock.getQuantity());
        beforeValue = String.valueOf(existence.getQuantity());

        existence.setQuantity(stock.getQuantity());

        var stockUpdated = repository.save(existence);
        saveLogUpdate(UpdateStockType.UPDATE_QUANTITY,stockUpdated);

    }

    @Override
    public Optional<Stock> fetchOrFailExistence(Stock stock) {
        var stockOptional = repository.findStock(stock)
                .orElseThrow(()-> new  StockNotFoundException());
        return Optional.of(stockOptional);
    }

    @Override
    public Optional<Stock> fetchExistence(Stock stock) {
        return repository.findStock(stock);
    }


    private Comparator<Stock> getProductComparator() {
        return (stock1, stock2) -> stock1.getProduct().getName().
                compareTo(stock2.getProduct().getName());
    }

    private void validateStock(Stock stock) {
        storageService.fetchOrFail(stock.getStorage().getStorageId());
        productService.fetchOrFail(stock.getProduct().getProductId());
    }


    public void saveLogUpdate(UpdateStockType type, Stock stock) {
        LogStock logStock = new LogStock();
        var user = userService.fetchOrFail(currentUserService.getCurrentUser().getUserId());
        logStock.setLote(stock.getLote());
        logStock.setMovementDate(systemDate);
        logStock.setType(type);
        logStock.setProduct(stock.getProduct());
        logStock.setBeforeValue(beforeValue);
        logStock.setUser(user.get());
        if(type.equals(UpdateStockType.UPDATE_COST)) {
            logStock.setAlteredValue(String.valueOf(stock.getCost()));
            logStock.setNewValue(String.valueOf(stock.getCost()));
        } else {
            logStock.setAlteredValue(String.valueOf(stock.getQuantity()));
            logStock.setNewValue(String.valueOf(stock.getQuantity()));
        }
        logStockService.save(logStock);
    }

}

package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.enums.MovementType;
import master.ao.storage.core.domain.models.Stock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockService {
    Stock saveOrUpdate(Stock stock, MovementType movementType);
    List<Stock> findByLocation(UUID locationId);
    List<Stock> findExistenceByProduct(UUID productId);
    List<Stock> findCriticalProducts(UUID storageId);
    List<Stock> findExpiredProducts(UUID storageId);
    List<Stock> findInventory(UUID storageId);
    void updateProductCost(Stock stock);
    void updateProductExistence(Stock stock);
    Optional<Stock> fetchOrFailExistence(Stock stock);
    Optional<Stock> fetchExistence(Stock stock);
    Long fetchExistenceByProduct(UUID productId,UUID storageId,String lote, String expirated, String model, Integer lifespan);
}

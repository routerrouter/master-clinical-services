package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.enums.MovementType;
import master.ao.storage.core.domain.models.Stock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockService {
    Stock saveOrUpdate(Stock stock, MovementType movementType);
    List<Stock> findByLocation(UUID locationId);
    List<Stock> findCriticalProducts(UUID storageId);
    List<Stock> findExpiredProducts(UUID storageId);
    List<Stock> findInventory(UUID storageId);
    void updateProductCost(Stock stock);
}

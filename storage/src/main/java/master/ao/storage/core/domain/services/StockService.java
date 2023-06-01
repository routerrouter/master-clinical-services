package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.enums.MovementType;
import master.ao.storage.core.domain.models.Stock;

public interface StockService {
    Stock saveOrUpdate(Stock stock, MovementType movementType);
}

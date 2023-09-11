package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.enums.UpdateStockType;
import master.ao.storage.core.domain.models.LogStock;

import java.time.LocalDate;
import java.util.List;

public interface LogStockService {
    LogStock save(LogStock logStock);
    List<LogStock> findAll(UpdateStockType type, LocalDate initialDate, LocalDate endDate);
}

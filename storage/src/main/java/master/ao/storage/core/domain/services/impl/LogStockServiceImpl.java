package master.ao.storage.core.domain.services.impl;


import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.enums.UpdateStockType;
import master.ao.storage.core.domain.models.LogStock;
import master.ao.storage.core.domain.repositories.LogStockRepository;
import master.ao.storage.core.domain.services.LogStockService;
import master.ao.storage.core.domain.utils.Converts;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogStockServiceImpl  implements LogStockService {

    private final LogStockRepository repository;
    private final Converts convert;


    @Override
    public LogStock save(LogStock logStock) {
        return repository.save(logStock);
    }

    @Override
    public List<LogStock> findAll(UpdateStockType type, LocalDate initialDate, LocalDate endDate) {

        List<LogStock> logReturned = new ArrayList<>();

        if (type != null) {
            logReturned = repository.findAll()
                    .stream()
                    .filter(log -> log.getType().equals(type))
                    .filter(log -> log.getMovementDate().isAfter(initialDate.minusDays(1)))
                    .filter(log -> log.getMovementDate().isBefore(endDate.plusDays(1)))
                    .sorted(getLogComparator())
                    .collect(Collectors.toList());
        } else {
            logReturned = repository.findAll()
                    .stream()
                    .filter(log -> log.getMovementDate().isAfter(initialDate.minusDays(1)))
                    .filter(log -> log.getMovementDate().isBefore(endDate.plusDays(1)))
                    .sorted(getLogComparator())
                    .collect(Collectors.toList());
        }

        return logReturned;

    }


    private Comparator<LogStock> getLogComparator() {
        return (log1, log2) ->
                log1.getMovementDate().
                        compareTo(log2.getMovementDate());
    }
}

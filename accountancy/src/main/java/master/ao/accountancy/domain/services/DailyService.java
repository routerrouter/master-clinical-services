package master.ao.accountancy.domain.services;

import master.ao.accountancy.api.requests.filters.DailyFilter;
import master.ao.accountancy.domain.models.Daily;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DailyService {
    Daily save(Daily daily);
    Daily edit(Daily daily, UUID dailyId);
    void updateStatusOrDelete(UUID dailyId);
    List<Daily> findDailyWithFilters(DailyFilter filter);
    Optional<Daily> findDetail(UUID dailyId);
    boolean advanceValue(UUID accountId);
}

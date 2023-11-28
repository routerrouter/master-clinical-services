package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.Quota;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuotaService {

    Optional<Quota> fetchOrFail(UUID natureId);
    Optional<Quota> findByYearAndNatureAndQuotaMonth(int year, UUID natureId, String month);
    Quota saveQuota(Quota quota);
    Quota updateQuota(Quota quota, UUID quotaId);
    List<Quota> findAll(UUID natureId);
}

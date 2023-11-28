package master.ao.accountancy.api.responses;


import lombok.Data;
import master.ao.accountancy.domain.models.AccountNature;
import master.ao.accountancy.domain.repositories.AccountNatureRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class QuotaResponse {
    private UUID quotaId;
    private int currentYear;
    private NatureResponse nature;
    private BigDecimal value;
}

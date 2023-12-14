package master.ao.accountancy.api.responses;


import lombok.Data;
import master.ao.accountancy.domain.models.AccountNature;
import master.ao.accountancy.domain.models.SubAccount;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FinancialProgrammingResponse {

    private UUID programmingId;
    private NatureDescriptionResponse nature;
    private SubAccountDescriptionResponse subAccount;
    private String document;
    private String description;
    private BigDecimal value;
    private String ncb;
    private String liq;
    private String os;
    private String finished;
    private String nif;

}

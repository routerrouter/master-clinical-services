package master.ao.accountancy.api.requests;


import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FinancialProgrammingRequest {

    @NotNull
    private UUID natureId;

    @NotNull
    private UUID subAccountId;

    @NotBlank
    private String document;

    private String description;

    @Min(1)
    private BigDecimal value;

    private String ncb = "";

    private String liq = "";

    private String os = "";

    private String finished = "NAO";

    @NotBlank
    private String nif;
}

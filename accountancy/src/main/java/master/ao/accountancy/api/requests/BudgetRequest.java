package master.ao.accountancy.api.requests;


import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BudgetRequest {

    @NotNull
    private UUID natureId;

    @NotNull
    @Min(1)
    private BigDecimal value;
}

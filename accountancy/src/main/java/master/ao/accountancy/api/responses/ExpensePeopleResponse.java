package master.ao.accountancy.api.responses;


import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ExpensePeopleResponse {
    private UUID expenseId;
    private int year;
    private NatureResponse nature;
    private BigDecimal value;
}

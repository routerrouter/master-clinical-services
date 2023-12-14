package master.ao.accountancy.api.responses;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ExpensePeopleResponse {
    private UUID expenseId;
    private LocalDateTime creationDate;
    private int year;
    private UserResponse user;
    private NatureDescriptionResponse nature;
    private BigDecimal value;

}

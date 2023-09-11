package master.ao.accountancy.api.responses;

import lombok.Data;
import master.ao.accountancy.domain.enums.AccountType;

import java.util.UUID;

@Data
public class AccountResponse {

    private UUID accountId;
    private String description;
    private String number;
    private AccountType accountType;
    private AccountClassResponse accountClass;

}

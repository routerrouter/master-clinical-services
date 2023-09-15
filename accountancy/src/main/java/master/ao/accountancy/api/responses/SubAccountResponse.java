package master.ao.accountancy.api.responses;


import lombok.Data;
import master.ao.accountancy.domain.models.Account;

import java.util.UUID;

@Data
public class SubAccountResponse {

    private UUID subAccountId;
    private String number;
    private String description;
    private AccountShortResponse account;
    private String movement;

}

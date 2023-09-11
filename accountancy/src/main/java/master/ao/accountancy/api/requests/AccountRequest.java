package master.ao.accountancy.api.requests;


import lombok.Getter;
import lombok.Setter;
import master.ao.accountancy.domain.enums.AccountType;
import master.ao.accountancy.domain.models.AccountClass;

@Getter
@Setter
public class AccountRequest {

    private String description;
    private String number;
    private AccountType accountType;
    private AccountClass accountClass;

}

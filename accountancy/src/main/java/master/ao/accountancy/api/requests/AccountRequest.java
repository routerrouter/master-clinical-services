package master.ao.accountancy.api.requests;


import lombok.Getter;
import lombok.Setter;
import master.ao.accountancy.domain.enums.AccountType;
import master.ao.accountancy.domain.models.AccountClass;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class AccountRequest {

    @NotBlank
    private String description;

    @NotBlank
    private String number;

    @NotNull
    private AccountType accountType;

    private UUID accountClassId;

}

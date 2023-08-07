package master.ao.accountancy.api.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AccountClassRequest {

    @NotBlank
    private String number;

    @NotBlank
    private String description;
}

package master.ao.accountancy.api.requests;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class SubAccountRequest {

    @NotBlank
    private String description;
    private String movement = "SIM";
    private UUID accountId;
}

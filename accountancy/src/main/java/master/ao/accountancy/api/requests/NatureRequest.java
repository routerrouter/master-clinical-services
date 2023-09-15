package master.ao.accountancy.api.requests;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class NatureRequest {
    private UUID natureId;

    @NotBlank
    private String description;

    private UUID categoryId;
}

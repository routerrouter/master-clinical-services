package master.ao.storage.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class GroupRequest {

    @NotBlank
    private String name;
}

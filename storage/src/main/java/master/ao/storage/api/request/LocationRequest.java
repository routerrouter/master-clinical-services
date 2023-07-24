package master.ao.storage.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Data
public class LocationRequest {

    @NotBlank
    private String shelf;

    @NotBlank
    private String partition;

}

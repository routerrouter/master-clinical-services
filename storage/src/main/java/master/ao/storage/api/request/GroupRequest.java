package master.ao.storage.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Data
public class GroupRequest {

    @NotBlank
    private String name;
    private LocalDateTime registeredAt = LocalDateTime.now(ZoneId.of("UTC"));
    private LocalDateTime lastUpdateAt = LocalDateTime.now(ZoneId.of("UTC"));
}

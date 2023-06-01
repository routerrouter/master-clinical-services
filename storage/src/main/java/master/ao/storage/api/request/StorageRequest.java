package master.ao.storage.api.request;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class StorageRequest {

    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Long capacity;
    private LocalDateTime registeredAt = LocalDateTime.now(ZoneId.of("UTC"));
    private LocalDateTime lastUpdateAt = LocalDateTime.now(ZoneId.of("UTC"));
}

package master.ao.storage.api.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LocationResponse {
    private UUID locationId;
    private LocalDateTime registeredAt;
    private String shelf;
    private String partition;
    private String description;
    private boolean enabeld;
}

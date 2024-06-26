package master.ao.storage.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {

    private UUID locationId;
    private LocalDateTime registeredAt;
    private String shelf;
    private String partition;
    private String description;
    private boolean enabeld;
}

package master.ao.storage.api.response;

import lombok.*;
import master.ao.storage.core.domain.enums.EntityType;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntityResponse {
    private UUID entityId;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String nif;
    private String responsible;
    private EntityType entityType;
    private boolean enabled;
}

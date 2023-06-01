package master.ao.storage.api.request;

import lombok.Data;
import master.ao.storage.core.domain.enums.EntityType;

@Data
public class EntityRequest {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String nif;
    private String responsible;
    private EntityType entityType;
    private boolean enabled;
}

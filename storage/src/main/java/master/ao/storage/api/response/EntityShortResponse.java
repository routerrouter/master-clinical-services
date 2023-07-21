package master.ao.storage.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import master.ao.storage.core.domain.enums.EntityType;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntityShortResponse {
    private UUID entityId;
    private String name;
}

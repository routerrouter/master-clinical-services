package master.ao.storage.api.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StorageResponse {
    private UUID storageId;
    private String name;
    private String description;
    private Long capacity;
}
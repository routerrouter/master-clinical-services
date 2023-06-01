package master.ao.storage.api.response;

import lombok.Data;

import java.util.UUID;

@Data
public class StorageResponse {
    private UUID storageId;
    private String name;
    private String description;
    private Long capacity;
}
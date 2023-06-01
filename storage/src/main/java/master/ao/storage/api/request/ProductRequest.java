package master.ao.storage.api.request;

import lombok.Data;
import master.ao.storage.core.domain.models.Category;
import master.ao.storage.core.domain.models.Group;
import master.ao.storage.core.domain.models.Storage;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductRequest {

    private UUID productId;
    private LocalDateTime registeredAt;
    private LocalDateTime lastUpdateAt;
    private String name;
    private Long criticalAmount;
    private Long minimumAmount;
    private String brand;
    private Category category;
    private Group group;
    private Storage storage;
    private UUID natureId;
}

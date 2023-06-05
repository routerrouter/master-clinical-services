package master.ao.storage.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

    private UUID productId;
    private LocalDateTime registeredAt;
    private String name;
    private Long criticalAmount;
    private Long minimumAmount;
    private String brand;
    private CategoryResponse category;
    private GroupResponse group;
    private StorageResponse storage;
    private UUID natureId;
}

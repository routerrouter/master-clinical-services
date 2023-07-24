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
public class ProductResponse {

    private UUID productId;
    private LocalDateTime registeredAt;
    private String name;
    private Long criticalAmount;
    private Long minimumAmount;
    private String brand;
    private CategoryResponse category;
    private GroupResponse group;
    private StorageShortResponse storage;
    private UUID natureId;
}

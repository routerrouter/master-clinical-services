package master.ao.storage.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import master.ao.storage.core.domain.enums.UnitType;
import master.ao.storage.core.domain.models.Location;
import master.ao.storage.core.domain.models.Movement;
import master.ao.storage.core.domain.models.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemsMovementResponse {
    private UUID id;
    private ProductShortResponse product;
    private LocationShortResponse location;
    private String lote;
    private String model;
    private String barcode;
    private String serialNumber;
    private UUID originTransfer;
    private UUID destineTransfer;
    private BigDecimal cost;
    private Long quantity;
    private UnitType unitType;
    private LocalDate acquisitionDate;
    private LocalDate registeredAt;
    private LocalDate expirationDate;
    private LocalDate manufactureDate;
    private Long lifespan;
}

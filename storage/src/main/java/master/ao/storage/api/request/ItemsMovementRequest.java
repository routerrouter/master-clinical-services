package master.ao.storage.api.request;

import lombok.Data;
import master.ao.storage.core.domain.enums.UnitType;
import master.ao.storage.core.domain.models.Location;
import master.ao.storage.core.domain.models.Movement;
import master.ao.storage.core.domain.models.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ItemsMovementRequest {
    private Product product;
    private Movement movement;
    private Location location;
    private String lote;
    private String model;
    private String barcode;
    private String serialNumber;
    private UUID originTransfer;
    private UUID destineTransfer;
    private BigDecimal cust;
    private Long quantity;
    private UnitType unitType;
    private LocalDate acquisitionDate;
    private LocalDate expirationDate;
    private LocalDate manufactureDate;
    private Long lifespan;
}

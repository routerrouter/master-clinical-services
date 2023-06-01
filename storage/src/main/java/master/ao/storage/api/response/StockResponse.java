package master.ao.storage.api.response;

import lombok.Getter;
import lombok.Setter;
import master.ao.storage.core.domain.enums.UnitType;
import master.ao.storage.core.domain.models.Location;
import master.ao.storage.core.domain.models.Product;
import master.ao.storage.core.domain.models.Storage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class StockResponse {
    private UUID uuid;
    private Product product;
    private Storage storage;
    private Location location;
    private String lote;
    private String model;
    private String barcode;
    private String serialNumber;
    private BigDecimal cust;
    private Long lifespan;
    private Long quantity;
    private UnitType unitType;
    private LocalDate acquisitionDate;
    private LocalDate expirationDate;
    private LocalDate manufactureDate;
}

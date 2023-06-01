package master.ao.storage.api.request;

import lombok.Getter;
import lombok.Setter;
import master.ao.storage.core.domain.enums.UnitType;
import master.ao.storage.core.domain.models.Location;
import master.ao.storage.core.domain.models.Product;
import master.ao.storage.core.domain.models.Storage;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class StockRequest {

    @NotNull
    private Product product;
    @NotNull
    private Storage storage;
    @NotNull
    private Location location;
    @NotBlank
    private String lote;
    private String model;
    private String barcode;
    private String serialNumber;
    @NotNull @Min(value = 1)
    private BigDecimal cust;
    private Long lifespan;
    @NotNull @Min(value = 1)
    private Long quantity;
    private UnitType unitType;
    private LocalDate acquisitionDate;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
}

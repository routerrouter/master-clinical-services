package master.ao.storage.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockResponse {
    private ProductResponse product;
    private StorageResponse storage;
    private LocationResponse location;
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

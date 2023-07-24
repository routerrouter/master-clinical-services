package master.ao.storage.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import master.ao.storage.core.domain.enums.UnitType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockResponse {
    private ProductShortResponse product;
    private StorageShortResponse storage;
    private LocationShortResponse location;
    private String lote;
    private String model;
    private String barcode;
    private String serialNumber;
    private BigDecimal cost;
    private Long lifespan;
    private Long quantity;
    private UnitType unitType;
    private LocalDate acquisitionDate;
    private LocalDate expirationDate;
    private LocalDate manufactureDate;
}

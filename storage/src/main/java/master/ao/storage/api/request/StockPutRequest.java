package master.ao.storage.api.request;

import lombok.Getter;
import lombok.Setter;
import master.ao.storage.core.domain.enums.UnitType;
import master.ao.storage.core.domain.models.Product;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class StockPutRequest {

    private Product product;
    private UUID storageId;
    private String lote;
    private String model;
    @Min(value = 1)
    private BigDecimal cost;
    private Long lifespan;
    private Long quantity;
    private LocalDate expirationDate;
}

package master.ao.storage.api.request;

import lombok.Getter;
import lombok.Setter;
import master.ao.storage.core.domain.enums.UnitType;
import master.ao.storage.core.domain.models.Location;
import master.ao.storage.core.domain.models.Product;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class ItemsTransferRequest {

    @NotNull
    private Product product;

    @NotNull
    private Location location;

    @NotNull
    private Long quantity;

    private String lote;
    private Integer lifespan;
    private LocalDate expirationDate;
    private String model;

    private UnitType unitType;
}

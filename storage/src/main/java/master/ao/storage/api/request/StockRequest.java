package master.ao.storage.api.request;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import master.ao.storage.core.domain.enums.UnitType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class StockRequest {

    public interface StockView {
        public static interface RegistrationPost {
        }

        public static interface CostPut {
        }

        public static interface QuantityPut {
        }

    }


    //@NotNull(groups = {StockView.RegistrationPost.class, StockView.CostPut.class})
    //@JsonView({StockView.RegistrationPost.class, StockView.CostPut.class})
    private UUID productId;

    //@NotNull(groups = {StockView.RegistrationPost.class, StockView.CostPut.class})
    //@JsonView({StockView.RegistrationPost.class, StockView.CostPut.class})
    private UUID storageId;


    //@NotNull(groups = {StockView.RegistrationPost.class})
    //@JsonView({StockView.RegistrationPost.class})
    private UUID locationId;

    //@JsonView({StockView.RegistrationPost.class, StockView.CostPut.class})
    private String lote;

    //@JsonView({StockView.RegistrationPost.class, StockView.CostPut.class})
    private String model;

    //@JsonView({StockView.RegistrationPost.class, StockView.CostPut.class})
    private String barcode;

    //@JsonView({StockView.RegistrationPost.class, StockView.CostPut.class})
    private String serialNumber;

    //@NotNull(groups = {StockView.RegistrationPost.class, StockView.CostPut.class})
    //@JsonView({StockView.RegistrationPost.class, StockView.CostPut.class})
    @Min(value = 1)
    private BigDecimal cost;

    /*@JsonView({
            StockView.RegistrationPost.class,
            StockView.CostPut.class,
            StockView.QuantityPut.class,
    })*/
    private Long lifespan;


    /*@JsonView({
            StockView.RegistrationPost.class,
            StockView.QuantityPut.class,
    })
    @NotNull(groups = {
            StockView.RegistrationPost.class,
            StockView.QuantityPut.class,
    })
    @Min(value = 1, groups = {
            StockView.RegistrationPost.class,
            StockView.QuantityPut.class,
    })*/
    private Long quantity;

    //@JsonView({StockView.RegistrationPost.class})
    private UnitType unitType;

    //@JsonView({StockView.RegistrationPost.class})
    private LocalDate acquisitionDate;

    //@JsonView({StockView.RegistrationPost.class})
    private LocalDate manufactureDate;

    /*@JsonView({
            StockView.RegistrationPost.class,
            StockView.QuantityPut.class,
            StockView.CostPut.class
    })*/
    private LocalDate expirationDate;
}

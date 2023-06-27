package master.ao.storage.api.response;

import lombok.Getter;
import lombok.Setter;
import master.ao.storage.core.domain.enums.TransferType;
import master.ao.storage.core.domain.enums.UnitType;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter

public class ItemsTransferResponse {

    private UUID itemTransferId;
    private ProductResponse product;
    private TransferResponse transfer;
    private LocationResponse location;
    private Long quantity;
    private String lote;
    private Long lifespan;
    private LocalDate expirationDate;
    private String model;
    private UnitType unitType;
    private TransferType type;

}

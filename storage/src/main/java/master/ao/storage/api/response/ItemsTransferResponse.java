package master.ao.storage.api.response;

import lombok.*;
import master.ao.storage.core.domain.enums.TransferType;
import master.ao.storage.core.domain.enums.UnitType;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemsTransferResponse {

    private UUID itemTransferId;
    private ProductShortResponse product;
    private LocationShortResponse location;
    private Long quantity;
    private String lote;
    private Long lifespan;
    private LocalDate expirationDate;
    private String model;
    private UnitType unitType;
    private TransferType type;

}

package master.ao.storage.api.request;

import lombok.Data;
import master.ao.storage.core.domain.enums.DevolutionType;
import master.ao.storage.core.domain.enums.MovementStatus;
import master.ao.storage.core.domain.enums.MovementType;
import master.ao.storage.core.domain.models.Entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class MovementRequest {
    private LocalDate movementDate;
    private UUID userId;
    private MovementType movementType;
    private String documentNumber;
    private Entities entity;
    private DevolutionType devolutionType;
    private BigDecimal total;
    private String description;
    private List<ItemsMovementRequest> items = new ArrayList<>();
}

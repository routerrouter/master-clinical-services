package master.ao.storage.api.response;

import lombok.Data;
import master.ao.storage.api.request.ItemsMovementRequest;
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
public class MovementResponse {
    private UUID movementId;
    private LocalDate movementDate;
    private UUID userId;
    private MovementType movementType;
    private String documentNumber;
    private Entities entity;
    private MovementStatus movementStatus;
    private DevolutionType devolutionType;
    private BigDecimal total;
    private String description;
    private List<ItemsMovementResponse> items = new ArrayList<>();
    private boolean criticalStockAlert;
}
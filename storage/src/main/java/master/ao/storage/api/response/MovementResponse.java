package master.ao.storage.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementResponse {
    private UUID movementId;
    private UUID userId;
    private LocalDate movementDate;
    private MovementType movementType;
    private String documentNumber;
    private EntityShortResponse entity;
    private MovementStatus movementStatus;
    private DevolutionType devolutionType;
    private BigDecimal total;
    private String description;
    private String patient;
    private boolean criticalStockAlert;
}

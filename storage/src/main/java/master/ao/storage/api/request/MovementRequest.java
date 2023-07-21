package master.ao.storage.api.request;

import lombok.Data;
import master.ao.storage.core.domain.enums.DevolutionType;
import master.ao.storage.core.domain.enums.MovementStatus;
import master.ao.storage.core.domain.enums.MovementType;
import master.ao.storage.core.domain.models.Entities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class MovementRequest {

    @NotNull
    private UUID entityId;

    @NotNull
    private LocalDate movementDate;

    @NotNull
    private MovementType movementType;

    @NotBlank
    private String documentNumber;

    @NotNull
    private DevolutionType devolutionType;
    private BigDecimal total;

    @NotBlank
    private String description;
    private String patient;
    

    @NotNull
    private List<ItemsMovementRequest> items = new ArrayList<>();
}

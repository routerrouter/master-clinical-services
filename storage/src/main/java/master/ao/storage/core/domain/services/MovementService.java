package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.ItemsMovement;
import master.ao.storage.core.domain.models.Movement;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MovementService {
    Movement save(Movement movement);
    List<Movement> listAndFilterAllMovements(Specification<Movement> spec, LocalDate initialDate, LocalDate endDate);
    List<ItemsMovement> listItemsByMovement(UUID movementId);

}

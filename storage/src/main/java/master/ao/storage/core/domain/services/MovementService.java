package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.ItemsMovement;
import master.ao.storage.core.domain.models.Movement;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface MovementService {
    Movement save(Movement movement, UUID userId);
    List<Movement> listAndFilterAllMovements(Specification<Movement> spec, UUID userId);
    List<ItemsMovement> listItemsByMovement(UUID movementId, UUID userId);

}

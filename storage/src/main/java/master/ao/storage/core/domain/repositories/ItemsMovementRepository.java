package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.ItemsMovement;
import master.ao.storage.core.domain.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemsMovementRepository extends JpaRepository<Location, UUID>, JpaSpecificationExecutor<ItemsMovement> {
}

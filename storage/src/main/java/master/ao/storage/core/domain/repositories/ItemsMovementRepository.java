package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.ItemsMovement;
import master.ao.storage.core.domain.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemsMovementRepository extends JpaRepository<Location, UUID> {

    @Query("select items From ItemsMovement  items where  items.movement.movementId = ?1")
    List<ItemsMovement> findAllByMovementId(@Param("movementId") UUID movementId);
}

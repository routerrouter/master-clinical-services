package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.Movement;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovementRepository extends JpaRepository<Movement, UUID>, JpaSpecificationExecutor<Movement> {
    @Query("select m from Movement  m where m.userGroup=?1")
    List<Movement> findAll(UUID userGroupId, Specification specification);

    @Modifying
    @Query("update Movement m set m.movementStatus='FINISHED' where m.movementId=:requestId")
    void updateMovementStatus(UUID requestId);

}
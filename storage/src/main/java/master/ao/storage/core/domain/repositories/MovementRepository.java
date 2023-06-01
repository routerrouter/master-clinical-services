package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovementRepository extends JpaRepository<Movement, UUID>, JpaSpecificationExecutor<Movement> {


}
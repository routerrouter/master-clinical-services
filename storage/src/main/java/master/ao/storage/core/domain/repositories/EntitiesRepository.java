package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.Entities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntitiesRepository extends JpaRepository<Entities, UUID>, JpaSpecificationExecutor<Entities> {

    Optional<Entities> findByName(String name);
    Optional<Entities> findById(UUID entityId);

}
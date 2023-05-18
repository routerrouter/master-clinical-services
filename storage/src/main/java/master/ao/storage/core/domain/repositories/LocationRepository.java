package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID>, JpaSpecificationExecutor<Location> {

    Optional<Location> findByDesription(String description);
    Optional<Location> findById(UUID locationId);

}
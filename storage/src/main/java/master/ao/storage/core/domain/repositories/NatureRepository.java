package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.Nature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NatureRepository extends JpaRepository<Nature, UUID>, JpaSpecificationExecutor<Nature> {

    Optional<Nature> findByName(String name);

    Optional<Nature> findById(UUID natureId);

}
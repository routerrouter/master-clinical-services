package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StorageRepository extends JpaRepository<Storage, UUID>, JpaSpecificationExecutor<Storage> {

    Optional<Storage> findByName(String name);
    Optional<Storage> findById(UUID storageId);

}
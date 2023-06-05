package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.Storage;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageService {
    Storage save(Storage Storage, UUID userId, String token);

    Storage update(Storage Storage, UUID storageId,String token);

    void disabledOrEnabled(UUID storageId);

    void delete(UUID storageId,String token);

    Optional<Storage> fetchOrFail(UUID storageId);

    List<Storage> findAll(Specification<Storage> spec);
}

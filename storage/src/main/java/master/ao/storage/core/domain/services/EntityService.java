package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.Entities;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntityService {
    Entities saveEndCreateAccount(Entities entity, UUID userId);

    Entities updateAndUpdateAccount(Entities entity, UUID entityId);

    void delete(UUID entityId);

    Optional<Entities> fetchOrFail(UUID entityId);

    List<Entities> findAll(Specification<Entities> spec);
}
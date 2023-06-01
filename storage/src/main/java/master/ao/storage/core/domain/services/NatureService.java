package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.Nature;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NatureService {
    Nature save(Nature nature, UUID userId);

    Nature update(Nature nature, UUID natureId);

    void delete(UUID natureId);

    Optional<Nature> fetchOrFail(UUID natureId);

    List<Nature> findAll(Specification<Nature> spec);
}
package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.Location;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationService {
    Location save(Location Location, UUID storageId);

    Location update(Location Location, UUID LocationId);

    void delete(UUID LocationId);

    Optional<Location> fetchOrFail(UUID LocationId);

    List<Location> findAll(Specification<Location> spec);
}
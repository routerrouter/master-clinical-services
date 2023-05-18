package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.Group;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupService {
    Group save(Group group);

    Group update(Group group, UUID groupId);

    void delete(UUID groupId);

    Optional<Group> fetchOrFail(UUID groupId);

    List<Group> findAll(Specification<Group> spec);
}
package master.ao.authuser.core.domain.service;

import master.ao.authuser.core.domain.model.Group;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupService {
    Group save(Group group);
    Group update(Group group, UUID groupId);
    void delete(UUID groupId);
    void disassociatePermission(UUID groupId, List<UUID> permissionId);
    void associatePermissions(UUID groupId, List<UUID> permissions);
    Optional<Group> fetchOrFail(UUID groupId);
    List<Group> findAll(Specification<Group> spec);
}

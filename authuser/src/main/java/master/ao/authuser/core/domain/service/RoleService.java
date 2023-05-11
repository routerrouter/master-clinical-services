package master.ao.authuser.core.domain.service;

import master.ao.authuser.core.domain.model.Role;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleService {
    Role save(Role role, UUID permissionId);
    Role update(Role role, UUID roleId);
    Optional<Role> fetchOrFail(UUID roleId);
    List<Role> findAll(Specification<Role> spec);
}

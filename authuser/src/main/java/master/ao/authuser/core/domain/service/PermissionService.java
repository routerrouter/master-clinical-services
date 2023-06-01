package master.ao.authuser.core.domain.service;

import master.ao.authuser.core.domain.model.Permission;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionService {
    Permission save(Permission permission);

    Permission update(Permission permission, UUID permisssionId);

    Optional<Permission> fetchOrFail(UUID permissionId);

    List<Permission> findAll(Specification<Permission> spec);
}

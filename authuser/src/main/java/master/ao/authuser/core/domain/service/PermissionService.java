package master.ao.authuser.core.domain.service;

import master.ao.authuser.core.domain.model.Permission;

import java.util.UUID;

public interface PermissionService {
    Permission save(Permission permission);
    Permission update(Permission permission, UUID permisssionId);
    Permission fetchOuFail(UUID permissionId);
}

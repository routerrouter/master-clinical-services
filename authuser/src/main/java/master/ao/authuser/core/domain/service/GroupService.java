package master.ao.authuser.core.domain.service;

import master.ao.authuser.core.domain.model.Group;

import java.util.UUID;

public interface GroupService {
    Group save(Group group);
    void delete(UUID groupId);
    void disassociatePermission(UUID groupId, UUID permissionId);
    void associatePermission(UUID groupId, UUID permissionId);
    Group fetchOrFail(UUID groupId);
}

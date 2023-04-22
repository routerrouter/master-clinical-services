package master.ao.authuser.core.domain.service.impl;

import master.ao.authuser.core.domain.exception.EntityInUseException;
import master.ao.authuser.core.domain.exception.GroupNotFoundException;
import master.ao.authuser.core.domain.model.Group;
import master.ao.authuser.core.domain.model.Permission;
import master.ao.authuser.core.domain.repository.GroupRepository;
import master.ao.authuser.core.domain.service.GroupService;
import master.ao.authuser.core.domain.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;


@Service
public class GroupServiceImpl implements GroupService {

    private static final String MSG_GRUPO_EM_USO
            = "Grupo de código %d não pode ser removido, pois está em uso";

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PermissionService permissionService;

    @Override
    @Transactional
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    @Transactional
    public void delete(UUID groupId) {
        try {
            groupRepository.deleteById(groupId);
            groupRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new GroupNotFoundException(groupId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(
                    String.format(MSG_GRUPO_EM_USO, groupId));
        }
    }

    @Override
    @Transactional
    public void disassociatePermission(UUID groupId, UUID permissionId) {
        Group group = fetchOrFail(groupId);
        Permission permission = permissionService.fetchOuFail(permissionId);

        group.removePermission(permission);
    }

    @Override
    @Transactional
    public void associatePermission(UUID groupId, UUID permissionId) {
        Group  group = fetchOrFail(groupId);
        Permission permission = permissionService.fetchOuFail(permissionId);

        group.addPermission(permission);
    }

    @Override
    public Group fetchOrFail(UUID groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
    }
}

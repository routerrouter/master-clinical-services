package master.ao.authuser.core.domain.service.impl;

import master.ao.authuser.core.domain.exception.PermissionNotFoundException;
import master.ao.authuser.core.domain.model.Permission;
import master.ao.authuser.core.domain.repository.PermissionRepository;
import master.ao.authuser.core.domain.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @Transactional
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission update(Permission permission, UUID permissionId) {
        Permission permissionExist = fetchOuFail(permissionId);
        return permissionRepository.save(permissionExist);
    }

    @Override
    public Permission fetchOuFail(UUID permissionId) {
        return permissionRepository.findById(permissionId)
                .orElseThrow(() -> new PermissionNotFoundException(permissionId));
    }
}

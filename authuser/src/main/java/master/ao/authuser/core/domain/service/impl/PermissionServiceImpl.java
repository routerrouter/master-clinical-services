package master.ao.authuser.core.domain.service.impl;

import lombok.RequiredArgsConstructor;
import master.ao.authuser.api.response.MenuView;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.exception.PermissionNotFoundException;
import master.ao.authuser.core.domain.model.Permission;
import master.ao.authuser.core.domain.repositories.PermissionRepository;
import master.ao.authuser.core.domain.service.PermissionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public Permission save(Permission permission) {
        var permissionOptional = permissionRepository.findByDescription(permission.getDescription());
        if (permissionOptional.isPresent()) {
            throw new BussinessException("Permissão informada já existe.");
        }
        permission.setRoute();
        return permissionRepository.save(permission);

    }

    @Override
    public Permission update(Permission permission, UUID permissionId) {
        var permissionExist = fetchOrFail(permissionId).get();

        permissionExist.setDescription(permission.getDescription());

        return permissionRepository.save(permissionExist);
    }


    @Override
    public List<Permission> findAll(Specification<Permission> spec) {
        return permissionRepository.findAll(spec);
    }

    @Override
    public List<Permission> findByAllByUser() {
        List<Permission> list = permissionRepository.findByAllByUser();
        return list;
    }

    @Override
    public Optional<Permission> fetchOrFail(UUID permissionId) {
        var permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new PermissionNotFoundException(permissionId));

        return Optional.of(permission);
    }


}

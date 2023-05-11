package master.ao.authuser.core.domain.service.impl;


import lombok.RequiredArgsConstructor;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.exception.RoleNotFoundException;
import master.ao.authuser.core.domain.model.Role;
import master.ao.authuser.core.domain.repository.RoleRepository;
import master.ao.authuser.core.domain.service.PermissionService;
import master.ao.authuser.core.domain.service.RoleService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final PermissionService permissionService;
    

    @Override
    public Role save(Role role, UUID permissionId) {
        var permission = permissionService.fetchOrFail(permissionId);

        var roleOptional = repository.findByDescription(role.getDescription());
        if (roleOptional.isPresent()) {
             throw new BussinessException("Acesso informado já existe!");
        }
        role.setPermission(permission.get());
        return repository.save(role);
    }

    @Override
    public Role update(Role role, UUID roleId) {
        var roleOptional = fetchOrFail(roleId).get();

        roleOptional.setDescription(role.getDescription());
        return repository.save(roleOptional);
    }

    @Override
    public Optional<Role> fetchOrFail(UUID roleId) {
        var role = repository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));

        return Optional.of(role);
    }

    @Override
    public List<Role> findAll(Specification<Role> spec) {
        return repository.findAll(spec);
    }

}

package master.ao.authuser.core.domain.service.impl;


import lombok.RequiredArgsConstructor;
import master.ao.authuser.api.mapper.RoleMapper;
import master.ao.authuser.api.response.RoleMenuResponse;
import master.ao.authuser.api.response.UserRoleAccesses;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.exception.RoleNotFoundException;
import master.ao.authuser.core.domain.model.Role;
import master.ao.authuser.core.domain.repositories.RoleRepository;
import master.ao.authuser.core.domain.service.PermissionService;
import master.ao.authuser.core.domain.service.RoleService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final PermissionService permissionService;
    private final RoleMapper mapper;


    @Override
    public Role save(Role role, UUID permissionId) {
        var permission = permissionService.fetchOrFail(permissionId);

        var roleOptional = repository.findByDescriptionAndPermission(role.getDescription(), permission.get());
        if (roleOptional.isPresent()) {
            throw new BussinessException("Acesso informado já existe para esta permissão!");
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

    @Override
    public Map<String, Object> findAllByUser(UUID userId) {

        List<RoleMenuResponse> roleMenu = new ArrayList<>();
        List<Role> roleList = repository.findAllByUser(userId);
        for (Role role : roleList) {
            roleMenu.add(mapper.toRoleResponseRoleMenuResponse(role));
        }

        List<UserRoleAccesses> userRoleAccessesList = new ArrayList<>();

        Map<Object, List<RoleMenuResponse>> listMap =
                roleMenu.stream()
                        .collect(Collectors.groupingBy(permission -> permission.getPermissionMenuResponse()));

        Map<String, Object> permissionRole = new HashMap<>();

        listMap
                .forEach((permission, roles) -> userRoleAccessesList
                        .add(new UserRoleAccesses(permission, roles)));


        permissionRole.put("MenuItem", userRoleAccessesList);
        return permissionRole;
    }

}

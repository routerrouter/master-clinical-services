package master.ao.authuser.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.authuser.api.request.RoleRequest;
import master.ao.authuser.api.response.PermissionMenuResponse;
import master.ao.authuser.api.response.RoleMenuResponse;
import master.ao.authuser.api.response.RoleResponse;
import master.ao.authuser.core.domain.model.Role;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final ModelMapper mapper;

    public Role toRole(RoleRequest request) {
        return mapper.map(request, Role.class);
    }

    public RoleResponse toRoleResponse(Role role) {
        return mapper.map(role, RoleResponse.class);
    }

    public RoleMenuResponse toRoleResponseRoleMenuResponse(Role role) {
        String toInit = role.getPermission().getDescription().replaceAll(" ","-");
        String toFinal = role.getDescription().replaceAll(" ","-");
        RoleMenuResponse menuResponse = new RoleMenuResponse();
        menuResponse.setLabel(role.getDescription());
        menuResponse.setTo("/"+toInit.toLowerCase().concat("/").concat(toFinal.toLowerCase()));

        PermissionMenuResponse permissionMenuResponse = new PermissionMenuResponse();
        permissionMenuResponse.setLabel(role.getPermission().getDescription());
        permissionMenuResponse.setIcon(role.getPermission().getIcon());
        permissionMenuResponse.setTo(role.getPermission().getRoute());
        menuResponse.setPermissionMenuResponse(permissionMenuResponse);
        return menuResponse;
    }

    public List<RoleResponse> toRoleResponseList(Page<Role> roles) {
        return roles.stream()
                .map(this::toRoleResponse)
                .collect(Collectors.toList());
    }
}
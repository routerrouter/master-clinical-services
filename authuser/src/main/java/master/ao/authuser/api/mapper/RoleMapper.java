package master.ao.authuser.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.authuser.api.request.RoleRequest;
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

    public RoleResponse toRoleResponse(Role Role) {
        return mapper.map(Role, RoleResponse.class);
    }

    public List<RoleResponse> toRoleResponseList(Page<Role> roles) {
        return roles.stream()
                .map(this::toRoleResponse)
                .collect(Collectors.toList());
    }
}
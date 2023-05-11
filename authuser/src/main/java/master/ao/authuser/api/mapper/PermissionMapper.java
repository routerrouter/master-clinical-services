package master.ao.authuser.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.authuser.api.request.PermissionRequest;
import master.ao.authuser.api.response.PermissionResponse;
import master.ao.authuser.core.domain.model.Permission;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionMapper {

    private final ModelMapper mapper;

    public Permission toPermission(PermissionRequest request){
        return mapper.map(request, Permission.class);
    }

    public PermissionResponse toPermissionResponse(Permission Permission) {
        return mapper.map(Permission, PermissionResponse.class);
    }

    public List<PermissionResponse> toPermissionResponseList(List<Permission> Permissions) {
        return Permissions.stream()
                .map(this::toPermissionResponse)
                .collect(Collectors.toList());
    }
}
package master.ao.authuser.api.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PermissionResponse {
    private UUID permissionId;
    private String description;
    private String icon;
    private String route;
    private List<RoleResponse> roles;
}

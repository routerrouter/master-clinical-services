package master.ao.authuser.api.response;

import lombok.Data;

import java.util.UUID;

@Data
public class PermissionResponse {
    private UUID permissionId;
    private String description;
}

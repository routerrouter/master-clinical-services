package master.ao.authuser.api.response;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleResponse {
    private UUID roleId;
    private String description;
}

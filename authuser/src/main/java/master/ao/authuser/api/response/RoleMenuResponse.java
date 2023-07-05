package master.ao.authuser.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.UUID;

@Data
public class RoleMenuResponse {
    private String label;
    private String to;
    @JsonIgnore
    private PermissionMenuResponse permissionMenuResponse;
}

package master.ao.authuser.api.request;

import lombok.Data;
import master.ao.authuser.core.domain.model.Permission;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Data
public class PermissionsGroupRequest {

    @NotBlank
    private UUID groupId;

    @NotNull
    private Set<Permission> permissions;
}

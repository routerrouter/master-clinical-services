package master.ao.authuser.api.response;

import lombok.*;
import master.ao.authuser.core.domain.model.Role;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleAccesses {
    private Object permission;
    private List<RoleMenuResponse> subs;

}

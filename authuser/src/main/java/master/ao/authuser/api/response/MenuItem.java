package master.ao.authuser.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import master.ao.authuser.api.response.PermissionMenuResponse;
import master.ao.authuser.api.response.SubMenu;
import master.ao.authuser.core.domain.model.Permission;
import master.ao.authuser.core.domain.model.Role;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MenuItem {
    private Object permission;
    private List<Role> subs;
}

package master.ao.authuser.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import master.ao.authuser.core.domain.model.Role;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserRoleAccsses {
    private String permission;
    private List<Role> roles;
}

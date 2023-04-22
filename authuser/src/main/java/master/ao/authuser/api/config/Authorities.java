package master.ao.authuser.api.config;

import lombok.Data;
import master.ao.authuser.core.domain.model.Permission;
import master.ao.authuser.core.domain.model.Role;

import java.time.LocalDate;
import java.util.List;

@Data
public class Authorities implements Comparable<Authorities>{

    private Permission permission;
    private List<Role> roles;

    public Authorities(Permission permission, List<Role> roles) {
        this.permission = permission;
        this.roles = roles;
    }


    public boolean equals(Authorities obj) {
        return obj.getPermission().equals(getPermission());
    }


    @Override
    public int compareTo(Authorities authority) {
        Authorities authorities = authority;
        return 1;
    }
}
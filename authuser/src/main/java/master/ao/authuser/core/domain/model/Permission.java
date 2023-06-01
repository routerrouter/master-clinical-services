package master.ao.authuser.core.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "PERMISSIONS")
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID permissionId;

    @Column(nullable = false)
    private String description;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "permission", fetch = FetchType.EAGER)
    //@Fetch(FetchMode.SUBSELECT)
    private Set<Role> roles;

    public boolean removeRole(Role role) {
        return getRoles().remove(role);
    }

    public boolean addRole(Role role) {
        return getRoles().add(role);
    }
}

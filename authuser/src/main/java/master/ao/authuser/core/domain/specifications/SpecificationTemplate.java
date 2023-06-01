package master.ao.authuser.core.domain.specifications;

import master.ao.authuser.core.domain.model.*;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "enabled", spec = Equal.class),
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "username", spec = Like.class),
            @Spec(path = "fullName", spec = Like.class)
    })
    public interface UserSpec extends Specification<User> {
    }

    public static Specification<User> usersGroupId(final UUID groupId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<User> user = root;
            Root<Group> group = query.from(Group.class);
            Expression<Collection<User>> usersGroup = group.get("users");
            return cb.and(cb.equal(group.get("groupId"), groupId), cb.isMember(user, usersGroup));
        };
    }


    @And({
            @Spec(path = "description", spec = Like.class)
    })
    public interface GroupSpec extends Specification<Group> {
    }

    @And({
            @Spec(path = "description", spec = Like.class)
    })
    public interface RoleSpec extends Specification<Role> {
    }

    public static Specification<Role> rolePermissionId(final UUID permissionId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Role> role = root;
            Root<Permission> permission = query.from(Permission.class);
            Expression<Collection<Role>> rolesPermissions = permission.get("roles");
            return cb.and(cb.equal(permission.get("permissionId"), permissionId), cb.isMember(role, rolesPermissions));
        };
    }

    public static Specification<Role> roleUserId(final UUID userId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Role> role = root;
            Root<User> user = query.from(User.class);
            Expression<Collection<Role>> rolesUser = user.get("roles");
            return cb.and(cb.equal(user.get("userId"), userId), cb.isMember(role, rolesUser));
        };
    }

    @And({
            @Spec(path = "description", spec = Like.class)
    })
    public interface PermissionSpec extends Specification<Permission> {
    }

    public static Specification<Permission> permissionGroupId(final UUID groupId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Permission> permission = root;
            Root<Group> group = query.from(Group.class);
            Expression<Collection<Permission>> groupPermissions = group.get("permissions");
            return cb.and(cb.equal(group.get("groupId"), groupId), cb.isMember(permission, groupPermissions));
        };
    }


}

package master.ao.storage.core.domain.specifications;

public class SpecificationTemplate {


  /*  @And({
            @Spec(path = "description", spec = Like.class)
    })
    public interface GroupSpec extends Specification<Group> {}

    public static Specification<Role> rolePermissionId(final UUID permissionId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Role> role = root;
            Root<Permission> permission = query.from(Permission.class);
            Expression<Collection<Role>> rolesPermissions = permission.get("roles");
            return cb.and(cb.equal(permission.get("permissionId"), permissionId), cb.isMember(role, rolesPermissions));
        };
    }*/


}

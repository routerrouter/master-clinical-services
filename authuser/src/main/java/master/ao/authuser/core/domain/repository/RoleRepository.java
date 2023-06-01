package master.ao.authuser.core.domain.repository;

import master.ao.authuser.core.domain.model.Permission;
import master.ao.authuser.core.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByDescriptionAndPermission(String name, Permission permission);

    @Query(value = "SELECT * FROM roles r INNER JOIN users_roles ur ON ur.role_id=r.role_id WHERE ur.user_id=:userId", nativeQuery = true)
    List<Role> findAllByUser(UUID userId);
}

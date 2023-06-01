package master.ao.authuser.core.domain.repository;

import master.ao.authuser.core.domain.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findById(UUID userId);

    @Modifying
    @Query(value = "DELETE FROM users_roles WHERE role_id=:roleId AND user_id=:userId", nativeQuery = true)
    void removeRoles(@Param("userId") UUID userId, @Param("roleId") UUID roleId);

    @Modifying
    @Query(value = "Insert INTO users_roles(user_id,role_id) values(:userId, :roleId)", nativeQuery = true)
    void associateRoles(UUID userId, UUID roleId);

    @Query(value = "SELECT count(user_id) FROM users_roles ur " +
            "where ur.user_id=:userId AND ur.role_id=:roleId", nativeQuery = true)
    long existUserRoles(@Param("userId") UUID userId, @Param("roleId") UUID roleId);


}

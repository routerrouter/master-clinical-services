package master.ao.authuser.core.domain.repository;

import master.ao.authuser.core.domain.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID>, JpaSpecificationExecutor<Group> {
    Optional<Group> findByDescription(String description);

    @Modifying
    @Query(value = "DELETE FROM groups_permissions WHERE group_id=:groupId AND permission_id=:permissionId", nativeQuery = true)
    void desassociatePermission(@Param("groupId") UUID groupId, @Param("permissionId") UUID permissionId);

    @Modifying
    @Query(value = "Insert INTO groups_permissions(group_id,permission_id) values(:groupId, :permissionId)", nativeQuery = true)
    void associatePermissions(UUID groupId, UUID permissionId);

    @Query(value = "SELECT count(group_id) FROM groups_permissions gp " +
            "where gp.group_id=:groupId AND gp.permission_id=:permissionId", nativeQuery = true)
    long existGroupPermission(@Param("groupId") UUID groupId, @Param("permissionId") UUID permissionId);

}

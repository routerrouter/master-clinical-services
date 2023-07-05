package master.ao.authuser.core.domain.repositories;

import master.ao.authuser.api.response.MenuView;
import master.ao.authuser.core.domain.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID>, JpaSpecificationExecutor<Permission> {
    Optional<Permission> findByDescription(String description);


    @Query(value = "select * from\n" +
            "\ttb_permissions tp\n" +
            "inner join tb_roles tr on\n" +
            "\ttp.permission_id = tr.permission_permission_id\n" +
            "inner join tb_users_roles tur on tur.role_id = tr.role_id \n" +
            "inner join tb_users tu  on tu.user_id =tur.user_id ",nativeQuery = true)
    List<Permission> findByAllByUser();
}

package master.ao.authuser.core.domain.repository;

import master.ao.authuser.core.domain.model.AcessLimitUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AccessLimitUserRepository extends JpaRepository<AcessLimitUser, UUID>, JpaSpecificationExecutor<AcessLimitUser> {

    @Query("Select ac from AcessLimitUser ac where ac.user.userId=:userID")
    Optional<AcessLimitUser> findAcessLimitUserByUser(@Param("userID") UUID userID);
}

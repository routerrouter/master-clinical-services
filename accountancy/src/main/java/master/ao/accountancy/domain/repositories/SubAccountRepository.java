package master.ao.accountancy.domain.repositories;


import master.ao.accountancy.domain.models.SubAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubAccountRepository extends JpaRepository<SubAccount, UUID>  , JpaSpecificationExecutor<SubAccount> {
    Optional<SubAccount> findByDescription(String description);
    Optional<SubAccount> findByNumber(String number);

    @Query("SELECT s FROM SubAccount s where s.number LIKE CONCAT('',:prefix,'%') order by s.number desc")
    List<SubAccount> findSubAccountsByPrefix(@Param("prefix") String prefix);

}

package master.ao.accountancy.domain.repositories;


import master.ao.accountancy.domain.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>  , JpaSpecificationExecutor<Account> {
    Optional<Account> findByDescription(String description);
    Optional<Account> findByNumber(String number);
}

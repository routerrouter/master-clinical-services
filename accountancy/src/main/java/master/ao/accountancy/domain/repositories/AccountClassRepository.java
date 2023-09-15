package master.ao.accountancy.domain.repositories;


import master.ao.accountancy.domain.models.AccountClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountClassRepository extends JpaRepository<AccountClass, UUID>  , JpaSpecificationExecutor<AccountClass> {
    Optional<AccountClass> findByDescription(String description);
    Optional<AccountClass> findByNumber(String number);
}

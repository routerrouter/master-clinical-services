package master.ao.accountancy.domain.repositories;


import master.ao.accountancy.domain.models.AccountNature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountNatureRepository extends JpaRepository<AccountNature, UUID>  , JpaSpecificationExecutor<AccountNature> {
    Optional<AccountNature> findByDescription(String description);
}

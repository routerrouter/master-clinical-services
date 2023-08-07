package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.AccountClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountClassService {
    AccountClass createAccountClass(AccountClass accountClass);
    AccountClass updateAccountClass(UUID accountClassId, AccountClass accountClass);
    Optional<AccountClass> fetchOrFail(UUID accountClassId);
    List<AccountClass> findAllAccountClass(Specification<AccountClass> specification);
    void delete(UUID accountClassId);
}

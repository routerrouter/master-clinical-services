package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.Account;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    Account createAccount(Account account);
    Account updateAccount(Account account, UUID accountId);
    Optional<Account> fetchOrFail(UUID accountId);
    List<Account> findAll(Specification<Account> specification);

}

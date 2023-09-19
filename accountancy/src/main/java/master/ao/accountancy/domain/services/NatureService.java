package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.AccountNature;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NatureService {

    AccountNature createAccountNature(AccountNature nature, UUID categoryId);
    AccountNature updateAccountNature(AccountNature nature, UUID accountId);
    Optional<AccountNature> fetchOrFail(UUID natureId);
    List<AccountNature> findAll(Specification<AccountNature> specification , UUID categoryId);
    void validateNature(AccountNature nature, UUID categoryId);
    void delete(UUID natureId);

}

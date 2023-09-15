package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.SubAccount;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubAccountService {
    SubAccount createSubAccount(SubAccount subAccount, UUID accountId);
    SubAccount updateSubAccount(SubAccount subAccount, UUID subAccountId);
    Optional<SubAccount> fetchOrFail(UUID subAccountId);
    List<SubAccount> findAll(Specification<SubAccount> specification, UUID accountId);
    void validateSubAccount(SubAccount subAccount, UUID accountId);
    void delete(UUID subAccountId);
}

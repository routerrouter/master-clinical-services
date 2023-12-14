package master.ao.accountancy.domain.services;

import master.ao.accountancy.api.responses.NatureDescriptionResponse;
import master.ao.accountancy.domain.models.ProviderNature;
import master.ao.accountancy.domain.models.SubAccount;
import master.ao.accountancy.domain.specifications.SpecificationTemplate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
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
    List<SubAccount> findAllProviders(SpecificationTemplate.SubAccountSpec spec);
    List<NatureDescriptionResponse> findAllNaturesByProviders(UUID providerId);
    void associateNatureToProvider(UUID providerId, List<UUID> naturesId);
}

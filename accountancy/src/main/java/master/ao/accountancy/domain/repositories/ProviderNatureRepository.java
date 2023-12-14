package master.ao.accountancy.domain.repositories;


import master.ao.accountancy.domain.models.ProviderNature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProviderNatureRepository  extends JpaRepository<ProviderNature, UUID> {

    @Query("Select n from  ProviderNature  n where n.subAccount.subAccountId=?1")
    List<ProviderNature> findAllNatureForProvider(UUID providerId);

    @Query("Select n from  ProviderNature  n where n.subAccount.subAccountId=?1 and n.nature.natureId=?2")
    Optional<ProviderNature> getIfExist(UUID providerId, UUID natureId);

    @Query(value = "Insert into tb_subaccount_natures(sub_account_id,nature_id) values(:subaccountId,:natureId) ", nativeQuery = true)
    void addNaturesToProvider(UUID subaccountId, UUID natureId);
}

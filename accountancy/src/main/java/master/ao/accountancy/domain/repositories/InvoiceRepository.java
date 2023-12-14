package master.ao.accountancy.domain.repositories;


import master.ao.accountancy.domain.models.InvoiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceProvider, UUID> {

    Optional<InvoiceProvider> findByInvoiceNumber(String number);

    @Query("SELECT  i from InvoiceProvider  i where i.subAccount.subAccountId=?1 and i.balance>0")
    List<InvoiceProvider> findByAccount(UUID accountId);

    @Query("SELECT  i from InvoiceProvider  i where i.invoiceNumber=?1 and i.subAccount.subAccountId=?2")
    Optional<InvoiceProvider> findByInvoiceNumberAndSubAccount(String number, UUID accountId);


}

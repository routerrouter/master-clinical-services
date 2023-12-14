package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.InvoiceProvider;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceService {

    void save(InvoiceProvider invoice);
    void update(InvoiceProvider invoice, UUID invoiceId);
    void delete(UUID invoiceId);
    Optional<InvoiceProvider> fetchOrFail(UUID invoiceId);
    Optional<InvoiceProvider> findLastInvoice(UUID invoiceId);
    Optional<InvoiceProvider> findByNumber(UUID accountId, String invoiceNumber);
    List<InvoiceProvider> findAllByInvoice(UUID invoiceId);
    List<InvoiceProvider> findAllByProvider(UUID accountId);
    List<InvoiceProvider> findBalanceEvolution(UUID accountId);
    List<InvoiceProvider> findAllByProviderAndNature(UUID accountId, UUID natureId);
    BigDecimal findValue(UUID accountId);
    BigDecimal findPaid(UUID accountId);
    BigDecimal findBalance(UUID accountId);
    BigDecimal findTotalBalance();

}

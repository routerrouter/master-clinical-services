package master.ao.accountancy.domain.services.implementation;

import lombok.RequiredArgsConstructor;
import master.ao.accountancy.domain.exceptions.ExistingDataException;
import master.ao.accountancy.domain.exceptions.InvoiceNotFoundException;
import master.ao.accountancy.domain.models.InvoiceProvider;
import master.ao.accountancy.domain.repositories.InvoiceRepository;
import master.ao.accountancy.domain.services.InvoiceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class InvoiceImpl implements InvoiceService {

    private final InvoiceRepository repository;

    @Override
    public void save(InvoiceProvider invoice) {
        findByNumber(invoice.getSubAccount().getSubAccountId(), invoice.getInvoiceNumber());
        invoice.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        invoice.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        invoice.setPaid(BigDecimal.ZERO);
        invoice.setBalance(invoice.getValue());

        repository.save(invoice);
    }

    @Override
    public void update(InvoiceProvider invoice, UUID invoiceId) {

    }

    @Override
    public void delete(UUID invoiceId) {
        fetchOrFail(invoiceId);
        repository.deleteById(invoiceId);
    }

    @Override
    public Optional<InvoiceProvider> fetchOrFail(UUID invoiceId) {
        var invoice = repository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
        return Optional.of(invoice);
    }

    @Override
    public Optional<InvoiceProvider> findLastInvoice(UUID invoiceId) {
        return null;
    }

    @Override
    public Optional<InvoiceProvider> findByNumber(UUID accountId, String invoiceNumber) {

        if (accountId != null) {
            var invoice = repository.findByInvoiceNumberAndSubAccount(invoiceNumber,accountId);
            if (invoice.isPresent()) {
               throw  new ExistingDataException("Já existe uma factura deste fornecedor com este número");
            } else {
                return invoice;
            }
        } else {
           return repository.findByInvoiceNumber(invoiceNumber);
        }

    }

    @Override
    public List<InvoiceProvider> findAllByInvoice(UUID invoiceId) {
        return null;
    }

    @Override
    public List<InvoiceProvider> findAllByProvider(UUID accountId) {

        if (accountId== null)
            return  repository.findAll();
        return repository.findByAccount(accountId);
    }

    @Override
    public List<InvoiceProvider> findBalanceEvolution(UUID accountId) {
        return null;
    }

    @Override
    public List<InvoiceProvider> findAllByProviderAndNature(UUID accountId, UUID natureId) {
        return null;
    }

    @Override
    public BigDecimal findValue(UUID accountId) {
        return findAllByProvider(accountId)
                .stream()
                .map(invoice -> BigDecimal.ONE.multiply(invoice.getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal findPaid(UUID accountId) {
        return findAllByProvider(accountId)
                .stream()
                .map(invoice -> BigDecimal.ONE.multiply(invoice.getPaid()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal findBalance(UUID accountId) {
        return findAllByProvider(accountId)
                .stream()
                .map(invoice -> BigDecimal.ONE.multiply(invoice.getBalance()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal findTotalBalance() {
        return findAllByProvider(null)
                .stream()
                .map(invoice -> BigDecimal.ONE.multiply(invoice.getBalance()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

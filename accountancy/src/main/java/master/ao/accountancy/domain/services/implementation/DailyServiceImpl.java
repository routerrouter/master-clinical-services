package master.ao.accountancy.domain.services.implementation;


import lombok.RequiredArgsConstructor;
import master.ao.accountancy.api.config.security.AuthenticationCurrentUserService;
import master.ao.accountancy.api.requests.filters.DailyFilter;
import master.ao.accountancy.domain.models.Daily;
import master.ao.accountancy.domain.models.InvoiceProvider;
import master.ao.accountancy.domain.models.SubAccount;
import master.ao.accountancy.domain.repositories.DailyRepository;
import master.ao.accountancy.domain.services.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DailyServiceImpl implements DailyService {

    private final DailyRepository dailyRepository;
    private final SubAccountService subAccountService;
    private final NatureService natureService;
    private final AuthenticationCurrentUserService currentUserService;
    private final InvoiceService invoiceService;
    private final DocumentService documentService;
    private BigDecimal advanceValue = BigDecimal.ZERO;

    @Override
    public Daily save(Daily daily) {
        daily.setUserId(currentUserService.getCurrentUser().getUserId());
        validateSubAccount(daily);
        saveExpenses(daily);

        daily.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        daily.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        var dailySaved = dailyRepository.save(daily);
        if (!dailySaved.getDocument().getDescription().equals("Fatura") || !dailySaved.equals("Factura")) {
            saveInvoice(dailySaved);
        }
        return dailySaved;
    }

    @Override
    public Daily edit(Daily daily, UUID dailyId) {
        return null;
    }

    @Override
    public void updateStatusOrDelete(UUID dailyId) {

    }

    @Override
    public List<Daily> findDailyWithFilters(DailyFilter filter) {
        return null;
    }

    @Override
    public Optional<Daily> findDetail(UUID dailyId) {
        return Optional.empty();
    }

    @Override
    public boolean advanceValue(UUID accountId) {
        double debitValue = dailyRepository.findDailyByAccount(accountId)
                .stream()
                .filter(daily -> daily.getDailyType().equals("DEBIT"))
                .mapToDouble(daily -> daily.getValue().doubleValue())
                .sum();

        double creditValue = dailyRepository.findDailyByAccount(accountId)
                .stream()
                .filter(daily -> daily.getDailyType().equals("CREDIT"))
                .mapToDouble(daily -> daily.getValue().doubleValue())
                .sum();

        advanceValue = BigDecimal.valueOf(debitValue).min(BigDecimal.valueOf(creditValue));

        return (debitValue > debitValue);
    }

    public InvoiceProvider setAndSaveInvoiceValuesAfterAdvance(Daily daily) {

        InvoiceProvider invoice = new InvoiceProvider();

        if (advanceValue(daily.getSubAccount().getSubAccountId())) {
            if (advanceValue.compareTo(daily.getValue()) == 1) {
                invoice.setPaid(daily.getValue());
                invoice.setBalance(BigDecimal.ZERO);
            } else {
                invoice.setPaid(advanceValue);
                invoice.setBalance(daily.getValue().min(advanceValue));
            }
        } else {
            invoice.setPaid(BigDecimal.ZERO);
            invoice.setBalance(daily.getValue());
        }

        invoice.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        invoice.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        invoice.setNature(daily.getNature());

        return invoice;
    }

    private void validateSubAccount(Daily daily) {
        var account = subAccountService.fetchOrFail(daily.getSubAccount().getSubAccountId());
        var nature = natureService.fetchOrFail(daily.getNature().getNatureId());
        var document = documentService.fetchOrFail(daily.getDocument().getDocumentId());

        daily.setSubAccount(account.get());
        daily.setNature(nature.get());
        daily.setDocument(document.get());


    }

    private void saveExpenses(Daily daily) {

    }

    private void saveInvoice(Daily daily) {
        InvoiceProvider invoice = new InvoiceProvider();

        invoice.setSubAccount(daily.getSubAccount());
        invoice.setValue(daily.getValue());
        invoice.setInvoiceDate(daily.getDailyDate());
        invoice.setInvoiceNumber(daily.getInvoiceNumber());
        invoice.setInvoiceUrl(daily.getDocumentUrl());
        invoice.setUserId(daily.getUserId());
        invoice.setDaily(daily);
        invoice.setSituation(2);
        invoice.setNature(daily.getNature());

        invoiceService.save(invoice);
    }
}

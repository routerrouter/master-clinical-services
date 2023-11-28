package master.ao.accountancy.domain.services.implementation;

import lombok.RequiredArgsConstructor;
import master.ao.accountancy.api.config.security.AuthenticationCurrentUserService;
import master.ao.accountancy.domain.exceptions.BudgetNotFoundException;
import master.ao.accountancy.domain.exceptions.QuotaNotFoundException;
import master.ao.accountancy.domain.models.AccountNature;
import master.ao.accountancy.domain.models.Budget;
import master.ao.accountancy.domain.models.Quota;
import master.ao.accountancy.domain.repositories.BudgetRepository;
import master.ao.accountancy.domain.repositories.QuotaRepository;
import master.ao.accountancy.domain.services.BudgetService;
import master.ao.accountancy.domain.services.NatureService;
import master.ao.accountancy.domain.services.QuotaService;
import master.ao.accountancy.domain.services.CurrentMonthService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuotaService {

    private final QuotaRepository quotaRepository;
    private final CurrentMonthService currentMonthService;
    private final AuthenticationCurrentUserService currentUserService;
    private final BudgetService budgetService;
    private final NatureService natureService;
    private final BudgetRepository budgetRepository;


    @Override
    public Optional<Quota> fetchOrFail(UUID quotaId) {
        var quotaOptional = quotaRepository.findById(quotaId)
                .orElseThrow(() -> new QuotaNotFoundException(quotaId));

        return Optional.of(quotaOptional);
    }

    @Override
    public Optional<Quota> findByYearAndNatureAndQuotaMonth(int year, UUID natureId, String month) {

        var quotaOptional = quotaRepository.findByYearAndNatureAndQuotaMonth(year,natureId, month)
                .orElseThrow(() -> new QuotaNotFoundException(year,natureId));

        return Optional.of(quotaOptional);
    }

    @Override
    public Quota saveQuota(Quota request) {

        var currentYear = currentMonthService.getActiveYear();
        validateNature(Optional.ofNullable(request.getNature()).get().getNatureId());
        validateBudget(currentYear.getYear(),request.getNature().getNatureId());
        validateBudgetValue(currentYear.getYear(),request.getNature().getNatureId(),request.getValue());

        request.setQuotaYear(currentYear.getYear());
        request.setQuotaMonth(currentYear.getMonth());

        var quotaOptional = quotaRepository.findByYearAndNatureAndQuotaMonth(currentYear.getYear(),request.getNature().getNatureId(), request.getQuotaMonth());
        if (quotaOptional.isPresent()) {
            throw new QuotaNotFoundException("Já existe uma Quota para a natureza, mês e ano selecionado!");
        }
        request.setUserId(currentUserService.getCurrentUser().getUserId());
        request.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        request.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        var quota = quotaRepository.save(request);

        if (quota.getQuotaId() != null) {
            var budget = budgetService.findByYearAndNature(quota.getQuotaYear(),quota.getNature().getNatureId()).get();
            budget.setAvailable(budget.getAvailable().add(quota.getValue()));
            budget.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            budgetRepository.save(budget);

            validateCategoryAndSaveExpense(quota.getNature(), quota.getValue());
        }

        return quota;

    }

    @Override
    public Quota updateQuota(Quota request, UUID quotaId) {
        var optionalQuota = fetchOrFail(quotaId);
        var quota = optionalQuota.get();
        quota.setNature(request.getNature());
        quota.setValue(request.getValue());
        quota.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return quotaRepository.save(quota);
    }

    @Override
    public List<Quota> findAll(UUID natureId) {
        if (natureId == null)
            return quotaRepository.findAll();
        return quotaRepository.findAllByNature(natureId);
    }

    public void validateBudget(int year,UUID natureId) {
        budgetService.findByYearAndNature(year,natureId);
    }

    public void validateBudgetValue(int year,UUID natureId, BigDecimal value) {
        var budget = budgetService.findByYearAndNature(year,natureId);
        if (budget.get().getValue().compareTo(value) == -1)
            throw new BudgetNotFoundException("O valor do Orçamento da Natureza é inferior ao Valor digitado. Por favor digite um valor inferior ou igual ao do Orçamento");

    }


    public void validateCategoryAndSaveExpense(AccountNature nature, BigDecimal value){
        if (nature.getCategory().getDescription().equalsIgnoreCase("Bens e Serviços")) {

        }
    }


    public void validateNature(UUID natureId) {
        natureService.fetchOrFail(natureId);
    }

}

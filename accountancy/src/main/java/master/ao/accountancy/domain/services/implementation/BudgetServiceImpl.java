package master.ao.accountancy.domain.services.implementation;

import lombok.RequiredArgsConstructor;
import master.ao.accountancy.api.config.security.AuthenticationCurrentUserService;
import master.ao.accountancy.domain.exceptions.BudgetNotFoundException;
import master.ao.accountancy.domain.exceptions.NatureNotFoundException;
import master.ao.accountancy.domain.models.Budget;
import master.ao.accountancy.domain.repositories.BudgetRepository;
import master.ao.accountancy.domain.repositories.UserRepository;
import master.ao.accountancy.domain.services.BudgetService;
import master.ao.accountancy.domain.services.CurrentMonthService;
import master.ao.accountancy.domain.services.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserService userService;
    private final CurrentMonthService currentMonthService;
    private final AuthenticationCurrentUserService currentUserService;


    @Override
    public Optional<Budget> fetchOrFail(UUID budgetId) {
        var budgetOptional = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new BudgetNotFoundException(budgetId));

        return Optional.of(budgetOptional);
    }

    @Override
    public Optional<Budget> findByYearAndNature(int year, UUID natureId) {

        var budgetOptional = budgetRepository.findByYearAndNature(year,natureId)
                .orElseThrow(() -> new BudgetNotFoundException(year,natureId));

        return Optional.of(budgetOptional);
    }

    @Override
    public Budget saveBudget(Budget budget) {

        int currentYear = currentMonthService.getActiveYear().getYear();

        var budgetOptional = budgetRepository.findByYearAndNature(currentYear,budget.getNature().getNatureId());
        if (budgetOptional.isPresent()) {
            throw new BudgetNotFoundException("Já existe um lançamento de abertura para a Natureza e Ano selecionado!");
        }
        var user = userService.fetchOrFail(currentUserService.getCurrentUser().getUserId());
        budget.setUser(user.get());
        budget.setCurrentYear(currentYear);
        budget.setAvailable(BigDecimal.ZERO);
        budget.setExecutable(BigDecimal.ZERO);
        budget.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        budget.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return budgetRepository.save(budget);

    }

    @Override
    public Budget updateBudget(Budget request, UUID budgetId) {
        var optionalBudget = fetchOrFail(budgetId);
        var budget = optionalBudget.get();
        budget.setNature(request.getNature());
        budget.setValue(request.getValue());
        budget.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return budgetRepository.save(budget);
    }


    @Override
    public void updateBudgetValues(BigDecimal value, UUID natureId, int year) {
        var optionalBudget = findByYearAndNature(year,natureId);
        var budget = optionalBudget.get();
        budget.setExecutable(budget.getExecutable().add(value));
        budget.setAvailable(budget.getAvailable().subtract(value));

        budgetRepository.save(budget);
    }

    @Override
    public void updateBudgetValuesOnEditOrRemoveMovement(BigDecimal oldValue,BigDecimal newValue, UUID natureId, int year) {
        var optionalBudget = findByYearAndNature(year,natureId);
        var budget = optionalBudget.get();
        budget.setExecutable(budget.getExecutable().subtract(oldValue).add(newValue));
        budget.setAvailable(budget.getAvailable().add(oldValue).subtract(newValue));

        budgetRepository.save(budget);
    }

    @Override
    public List<Budget> findAll(UUID natureId) {
        if (natureId == null)
            return budgetRepository.findAll();
        return budgetRepository.findAllByNature(natureId);
    }
}

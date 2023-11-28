package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.Budget;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetService {

    Optional<Budget> fetchOrFail(UUID natureId);
    Optional<Budget> findByYearAndNature(int year, UUID natureId);
    Budget saveBudget(Budget budget);
    Budget updateBudget(Budget budget, UUID budgetId);
    void updateBudgetValues(BigDecimal value, UUID natureId, int year);
    void updateBudgetValuesOnEditOrRemoveMovement(BigDecimal oldValue,BigDecimal newValue, UUID natureId, int year);
    List<Budget> findAll(UUID natureId);
}

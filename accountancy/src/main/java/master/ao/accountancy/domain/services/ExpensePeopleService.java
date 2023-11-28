package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.ExpensePeople;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpensePeopleService {

    Optional<ExpensePeople> fetchOrFail(UUID natureId);
    Optional<ExpensePeople> findByYearAndNature(int year, UUID natureId);
    ExpensePeople saveExpensePeople(ExpensePeople expensePeople);
    ExpensePeople updateExpensePeople(ExpensePeople expensePeople, UUID expensePeopleId);
    void updateExpensePeopleValues(BigDecimal value, UUID natureId, int year);
    List<ExpensePeople> findAll(UUID natureId);


}

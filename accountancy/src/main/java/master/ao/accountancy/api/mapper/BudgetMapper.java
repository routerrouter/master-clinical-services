package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.BudgetRequest;
import master.ao.accountancy.api.responses.BudgetResponse;
import master.ao.accountancy.domain.models.Budget;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BudgetMapper {

    private final ModelMapper mapper;

    public Budget toBudget(BudgetRequest request) {
        return mapper.map(request, Budget.class);
    }

    public BudgetResponse toBudgetResponse(Budget budget) {
        return mapper.map(budget, BudgetResponse.class);
    }

    public List<BudgetResponse> toBudgetResponseList(List<Budget> budgetList) {
        return budgetList.stream()
                .map(this::toBudgetResponse)
                .collect(Collectors.toList());
    }
}

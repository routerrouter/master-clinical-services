package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.ExpensePeopleRequest;
import master.ao.accountancy.api.responses.ExpensePeopleResponse;
import master.ao.accountancy.domain.models.ExpensePeople;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ExpensePeopleMapper {

    private final ModelMapper mapper;

    public ExpensePeople toExpensePeople(ExpensePeopleRequest request) {
        return mapper.map(request, ExpensePeople.class);
    }

    public ExpensePeopleResponse toExpensePeopleResponse(ExpensePeople expensePeople) {
        return mapper.map(expensePeople, ExpensePeopleResponse.class);
    }

    public List<ExpensePeopleResponse> toExpensePeopleResponseList(List<ExpensePeople> expensePeopleList) {
        return expensePeopleList.stream()
                .map(this::toExpensePeopleResponse)
                .collect(Collectors.toList());
    }
}

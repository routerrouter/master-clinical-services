package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.AccountClassRequest;
import master.ao.accountancy.api.responses.AccountClassResponse;
import master.ao.accountancy.domain.models.AccountClass;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AccountClassMapper {

    private final ModelMapper mapper;

    public AccountClass toAccountClass(AccountClassRequest request) {
        return mapper.map(request, AccountClass.class);
    }

    public AccountClassResponse toAccountClassResponse(AccountClass accountClass) {
        return mapper.map(accountClass, AccountClassResponse.class);
    }

    public List<AccountClassResponse> toCategoryResponseList(List<AccountClass> accountClassList) {
        return accountClassList.stream()
                .map(this::toAccountClassResponse)
                .collect(Collectors.toList());
    }
}

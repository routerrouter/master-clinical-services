package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.AccountRequest;
import master.ao.accountancy.api.responses.AccountResponse;
import master.ao.accountancy.domain.models.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AccountMapper {

    private final ModelMapper mapper;

    public Account toAccountClass(AccountRequest request) {
        return mapper.map(request, Account.class);
    }

    public AccountResponse toAccountResponse(Account account) {
        return mapper.map(account, AccountResponse.class);
    }

    public List<AccountResponse> toAccountResponseList(List<Account> accountList) {
        return accountList.stream()
                .map(this::toAccountResponse)
                .collect(Collectors.toList());
    }
}

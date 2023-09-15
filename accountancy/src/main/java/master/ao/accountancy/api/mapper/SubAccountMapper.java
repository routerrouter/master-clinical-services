package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.SubAccountRequest;
import master.ao.accountancy.api.responses.SubAccountResponse;
import master.ao.accountancy.domain.models.SubAccount;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SubAccountMapper {

    private final ModelMapper mapper;

    public SubAccount toSubAccount(SubAccountRequest request) {
        return mapper.map(request, SubAccount.class);
    }

    public SubAccountResponse toSubAccountResponse(SubAccount subAccount) {
        return mapper.map(subAccount, SubAccountResponse.class);
    }

    public List<SubAccountResponse> toSubAccountResponseList(List<SubAccount> subAccountList) {
        return subAccountList.stream()
                .map(this::toSubAccountResponse)
                .collect(Collectors.toList());
    }
}

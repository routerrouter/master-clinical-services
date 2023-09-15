package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.NatureRequest;
import master.ao.accountancy.api.responses.NatureResponse;
import master.ao.accountancy.domain.models.AccountNature;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class NatureMapper {

    private final ModelMapper mapper;

    public AccountNature toAccountNature(NatureRequest request) {
        return mapper.map(request, AccountNature.class);
    }

    public NatureResponse toNatureResponse(AccountNature nature) {
        return mapper.map(nature, NatureResponse.class);
    }

    public List<NatureResponse> toNatureResponseList(List<AccountNature> natureList) {
        return natureList.stream()
                .map(this::toNatureResponse)
                .collect(Collectors.toList());
    }
}

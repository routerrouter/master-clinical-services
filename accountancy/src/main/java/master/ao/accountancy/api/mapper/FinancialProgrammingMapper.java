package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.FinancialProgrammingRequest;
import master.ao.accountancy.api.responses.FinancialProgrammingResponse;
import master.ao.accountancy.domain.models.FinancialProgramming;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FinancialProgrammingMapper {

    private final ModelMapper mapper;

    public FinancialProgramming toFinancialProgramming(FinancialProgrammingRequest request) {
        return mapper.map(request, FinancialProgramming.class);
    }

    public List<FinancialProgramming>  toFinancialProgrammingList(List<FinancialProgrammingRequest> financialProgrammingList) {
        return financialProgrammingList.stream()
                .map(this::toFinancialProgramming)
                .collect(Collectors.toList());
    }

    public FinancialProgrammingResponse toFinancialProgrammingResponse(FinancialProgramming financialProgramming) {
        return mapper.map(financialProgramming, FinancialProgrammingResponse.class);
    }

    public List<FinancialProgrammingResponse> toFinancialProgrammingResponseList(List<FinancialProgramming> financialProgrammingList) {
        return financialProgrammingList.stream()
                .map(this::toFinancialProgrammingResponse)
                .collect(Collectors.toList());
    }
}

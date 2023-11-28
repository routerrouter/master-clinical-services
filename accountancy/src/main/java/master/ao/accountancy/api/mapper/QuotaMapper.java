package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.QuotaRequest;
import master.ao.accountancy.api.responses.QuotaResponse;
import master.ao.accountancy.domain.models.Quota;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class QuotaMapper {

    private final ModelMapper mapper;

    public Quota toQuota(QuotaRequest request) {
        return mapper.map(request, Quota.class);
    }

    public QuotaResponse toQuotaResponse(Quota  quota) {
        return mapper.map(quota, QuotaResponse.class);
    }

    public List<QuotaResponse> toQuotaResponseList(List<Quota> quotaList) {
        return quotaList.stream()
                .map(this::toQuotaResponse)
                .collect(Collectors.toList());
    }
}

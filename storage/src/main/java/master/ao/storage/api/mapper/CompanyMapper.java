package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.CompanyRequest;
import master.ao.storage.api.response.CompanyResponse;
import master.ao.storage.core.domain.models.Company;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyMapper {

    private final ModelMapper mapper;

    public Company toCompany(CompanyRequest request) {
        return mapper.map(request, Company.class);
    }

    public CompanyResponse toCompanyResponse(Company company) {
        return mapper.map(company, CompanyResponse.class);
    }

}
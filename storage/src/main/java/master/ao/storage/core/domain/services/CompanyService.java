package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.Company;
import master.ao.storage.core.domain.models.CompanyImage;
import master.ao.storage.core.domain.wrappers.CompanyWrapper;

import java.util.Optional;
import java.util.UUID;

public interface CompanyService {
    Company createOrUpdateDetails(Company company,UUID companyId);
    Optional<Company> findDetails();
    Optional<Company> fetchOrFail(UUID companyId);
    CompanyImage uploadImage(CompanyWrapper wrapper);
}

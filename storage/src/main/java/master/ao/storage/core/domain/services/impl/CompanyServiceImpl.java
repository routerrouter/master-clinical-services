package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.exceptions.CompanyNotFoundException;
import master.ao.storage.core.domain.exceptions.GroupNotFoundException;
import master.ao.storage.core.domain.models.Company;
import master.ao.storage.core.domain.models.CompanyImage;
import master.ao.storage.core.domain.models.Group;
import master.ao.storage.core.domain.repositories.CompanyImageRepository;
import master.ao.storage.core.domain.repositories.CompanyRepository;
import master.ao.storage.core.domain.services.CompanyService;
import master.ao.storage.core.domain.wrappers.CompanyWrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository repository;
    private final CompanyImageRepository companyImageRepository;

    @Override
    public Company createOrUpdateDetails(Company company, UUID companyId) {
        var companyOptional = fetchOrFail(companyId).get();
        company.setCompanyId(companyId);
        company.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        company.setCreationDate(companyOptional.getCreationDate());
        return repository.save(company);
    }

    @Override
    public Optional<Company> findDetails() {
        return repository.findAll().stream().limit(1).findFirst();
    }


    @Override
    public CompanyImage uploadImage(CompanyWrapper companyWrapper) {
        CompanyImage companyImage = new CompanyImage();
        var existImageUploaded = companyImageRepository.findAll().stream().findFirst();
        if (existImageUploaded.isPresent()) {
            companyImage.setId(existImageUploaded.get().getId());
        }
        companyImage.setCompany(repository.findById(companyWrapper.getCompanyId()).get());
        companyImage.setContentType(companyWrapper.getContentType());
        companyImage.setBase64(companyWrapper.getBase64());
        return companyImageRepository.save(companyImage);
    }

    @Override
    public Optional<Company> fetchOrFail(UUID companyId) {
        var company = repository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        return Optional.of(company);
    }


}

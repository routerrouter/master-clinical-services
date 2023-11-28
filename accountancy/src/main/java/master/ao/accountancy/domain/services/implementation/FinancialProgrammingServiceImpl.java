package master.ao.accountancy.domain.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import master.ao.accountancy.api.config.security.AuthenticationCurrentUserService;
import master.ao.accountancy.domain.models.FinancialProgramming;
import master.ao.accountancy.domain.repositories.FinancialProgrammingRepository;
import master.ao.accountancy.domain.services.CurrentMonthService;
import master.ao.accountancy.domain.services.FinancialProgrammingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialProgrammingServiceImpl implements FinancialProgrammingService {


    private final FinancialProgrammingRepository repository;
    private final CurrentMonthService currentMonthService;
    private final AuthenticationCurrentUserService currentUserService;

    @Override
    public FinancialProgramming save(FinancialProgramming programming) {

        var currentYear = currentMonthService.getActiveYear();

        programming.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        programming.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        programming.setUserId(currentUserService.getCurrentUser().getUserId());
        programming.setProgrammingYear(currentYear.getYear());
        programming.setProgrammingMonth(currentYear.getMonth());

        verifyExistence(currentYear.getYear(),currentYear.getMonth(),programming.getNature().getNatureId(),programming.getSubAccount().getSubAccountId());

        return null;
    }

    @Override
    public void delete(UUID programmingId) {

    }

    @Override
    public void update(UUID programmingId) {

    }

    @Override
    public FinancialProgramming finallyProgramming(int year, String month) {
        return null;
    }

    @Override
    public List<FinancialProgramming> findAllProgramming() {
        return null;
    }

    @Override
    public void verifyExistence(int year, String month, UUID natureId, UUID providerId) {

        if (repository.verifyExistenceByProvider(year,month,natureId,providerId).isPresent()) {
            log.debug("Já existe uma programação financeira para a natureza:{} e subconta: {}",natureId,providerId);
        }

    }

    @Override
    public void finallyVerify(int year, String month) {

    }

    @Override
    public void verifyExistence(int year, String month) {

    }

    @Override
    public void linesVerifying() {

    }

    @Override
    public void validateDocument(int year, String month) {

    }

    @Override
    public List<FinancialProgramming> getAll(int year, String month) {
        return null;
    }

    @Override
    public void updateValueAt(int document, String valueNcb, String valueLiq, String valueOs, UUID programmingId) {

    }

}

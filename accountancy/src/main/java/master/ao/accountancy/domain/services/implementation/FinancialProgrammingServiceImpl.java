package master.ao.accountancy.domain.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import master.ao.accountancy.api.config.security.AuthenticationCurrentUserService;
import master.ao.accountancy.domain.exceptions.ExistingDataException;
import master.ao.accountancy.domain.exceptions.FinancialProgrammingNotFoundException;
import master.ao.accountancy.domain.models.FinancialProgramming;
import master.ao.accountancy.domain.repositories.FinancialProgrammingRepository;
import master.ao.accountancy.domain.services.CurrentMonthService;
import master.ao.accountancy.domain.services.FinancialProgrammingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialProgrammingServiceImpl implements FinancialProgrammingService {


    private final FinancialProgrammingRepository repository;
    private final CurrentMonthService currentMonthService;
    private final AuthenticationCurrentUserService currentUserService;


    @Override
    public void create(List<FinancialProgramming> programmingRequested) {

        var currentYear = currentMonthService.getActiveYear();
        finallyVerify(currentYear.getYear(),currentYear.getMonth(),"Não é possivel Fazer a Programação neste Periodo porque já foi Finalizada. Favor selecionar outro periodo.");
        programmingRequested.forEach(financialProgramming -> {
            financialProgramming.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
            financialProgramming.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            financialProgramming.setUserId(currentUserService.getCurrentUser().getUserId());
            financialProgramming.setProgrammingYear(currentYear.getYear());
            financialProgramming.setProgrammingMonth(currentYear.getMonth());
            financialProgramming.setFinished("NAO");

            verifyExistence(currentYear.getYear(),currentYear.getMonth(),financialProgramming.getNature().getNatureId(),financialProgramming.getSubAccount().getSubAccountId());
            repository.save(financialProgramming);
        });

    }

    @Override
    public void delete(UUID programmingId) {
        var programmingOptional = repository.findById(programmingId)
                .orElseThrow(() -> new FinancialProgrammingNotFoundException(programmingId));

        repository.delete(programmingOptional);
    }

    @Override
    public void update(FinancialProgramming programmingRequested,UUID programmingId) {
        var currentYear = currentMonthService.getActiveYear();

        var programmingOptional = fetchOrFail(programmingId);

        finallyVerify(currentYear.getYear(), currentYear.getMonth()," Não é possivel editar estas informações porque já se econtram Finalizadas</label> <br>Para poder reeditar deverá fazer a rebertura do ano vigente.");

        FinancialProgramming programming = programmingOptional.get();

        programmingRequested.setProgrammingId(programmingId);
        BeanUtils.copyProperties(programmingRequested,programming);

        repository.save(programming);



    }

    @Transactional
    @Override
    public void finallyProgramming() {
        var currentYear = currentMonthService.getActiveYear();
        validateDocument(currentYear.getYear(), currentYear.getMonth());
        repository.finallyProgramming(currentYear.getYear(), currentYear.getMonth());
    }

    @Override
    public Optional<FinancialProgramming> fetchOrFail(UUID programmingId) {
        var programmingOptional = repository.findById(programmingId)
                .orElseThrow(() -> new FinancialProgrammingNotFoundException(programmingId));
        return Optional.of(programmingOptional);
    }

    @Override
    public List<FinancialProgramming> findAllProgramming(UUID natureId) {
        if (natureId == null)
            return repository.findAll();
        return repository.findAll(natureId);
    }

    @Override
    public void verifyExistence(int year, String month, UUID natureId, UUID providerId) {

        if (repository.verifyExistenceByProvider(year,month,natureId,providerId).isPresent()) {
            throw  new ExistingDataException("Já existe uma programação financeira para a natureza:{} e subconta: {}");
        }

    }

    @Override
    public void finallyVerify(int year, String month, String message) {
        if (repository.finallyVerify(year, month).isPresent()) {
            new FinancialProgrammingNotFoundException(message);
        }

    }

    @Override
    public void verifyExistence(int year, String month) {

    }

    @Override
    public void linesVerifying() {

    }

    @Override
    public void validateDocument(int year, String month) {
        repository.validateDocument(year,month)
        .orElseThrow(() -> new FinancialProgrammingNotFoundException("Foram encontradas Programações sem os dados dos Documentos</label><br> Por favor, acessar a janela de Programação para inserir este dados.</html>"));
    }

    @Override
    public List<FinancialProgramming> getAll(int year, String month) {
        return null;
    }

    @Override
    public void updateValueAt(FinancialProgramming financialProgramming, UUID programmingId) {

        var programming = repository.findById(programmingId)
                .orElseThrow(() -> new FinancialProgrammingNotFoundException(programmingId));

        programming.setLiq(financialProgramming.getLiq());
        programming.setOs(financialProgramming.getOs());
        programming.setDocument(financialProgramming.getDocument());
        programming.setNcb(financialProgramming.getNcb());
        programming.setValue(financialProgramming.getValue());
        repository.save(programming);
    }


}

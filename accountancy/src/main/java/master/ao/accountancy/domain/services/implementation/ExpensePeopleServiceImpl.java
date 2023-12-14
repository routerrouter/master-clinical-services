package master.ao.accountancy.domain.services.implementation;

import lombok.RequiredArgsConstructor;
import master.ao.accountancy.api.config.security.AuthenticationCurrentUserService;
import master.ao.accountancy.domain.exceptions.ExpensePeopleNotFoundException;
import master.ao.accountancy.domain.models.ExpensePeople;
import master.ao.accountancy.domain.repositories.ExpensePeopleRepository;
import master.ao.accountancy.domain.services.ExpensePeopleService;
import master.ao.accountancy.domain.services.NatureService;
import master.ao.accountancy.domain.services.UserService;
import master.ao.accountancy.domain.services.UtilService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ExpensePeopleServiceImpl implements ExpensePeopleService {

    private final ExpensePeopleRepository expensePeopleRepository;
    private final UtilService utilService;
    private final NatureService natureService;
    private final UserService userService;
    private final AuthenticationCurrentUserService currentUserService;


    @Override
    public Optional<ExpensePeople> fetchOrFail(UUID expensePeopleId) {
        var expensePeopleOptional = expensePeopleRepository.findById(expensePeopleId)
                .orElseThrow(() -> new ExpensePeopleNotFoundException(expensePeopleId));

        return Optional.of(expensePeopleOptional);
    }

    @Override
    public Optional<ExpensePeople> findByYearAndNature(int year, UUID natureId) {

        var expensePeopleOptional = expensePeopleRepository.findByYearAndNature(year,natureId)
                .orElseThrow(() -> new ExpensePeopleNotFoundException(natureId));

        return Optional.of(expensePeopleOptional);
    }

    @Override
    public ExpensePeople saveExpensePeople(ExpensePeople expensePeople) {

        var currentYear = utilService.getCurrentPeriod();
        validateNature(Optional.ofNullable(expensePeople.getNature()).get().getNatureId());

        var expensePeopleOptional = expensePeopleRepository.findByYearAndNature(currentYear.getYear(),expensePeople.getNature().getNatureId());
        if (expensePeopleOptional.isPresent()) {
            throw new ExpensePeopleNotFoundException("Já existe um lançamento de despesas para a natureza selecionada!");
        }
        var user = userService.fetchOrFail(currentUserService.getCurrentUser().getUserId());
        expensePeople.setUser(user.get());
        expensePeople.setYear(currentYear.getYear());
        expensePeople.setMonth(currentYear.getMonth());
        expensePeople.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        expensePeople.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return expensePeopleRepository.save(expensePeople);

    }

    @Override
    public ExpensePeople updateExpensePeople(ExpensePeople request, UUID expensePeopleId) {
        var optionalExpensePeople = fetchOrFail(expensePeopleId);
        var expensePeople = optionalExpensePeople.get();
        expensePeople.setNature(request.getNature());
        expensePeople.setValue(request.getValue());
        expensePeople.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return expensePeopleRepository.save(expensePeople);
    }


    @Override
    public void updateExpensePeopleValues(BigDecimal value, UUID natureId, int year) {
        var optionalExpensePeople = findByYearAndNature(year,natureId);
        var expensePeople = optionalExpensePeople.get();

        expensePeopleRepository.save(expensePeople);
    }


    @Override
    public List<ExpensePeople> findAll(UUID natureId) {
        if (natureId == null)
            return expensePeopleRepository.findAll();
        return expensePeopleRepository.findAllByNature(natureId);
    }


    public void validateNature(UUID natureId) {
        natureService.fetchOrFail(natureId);
    }
}

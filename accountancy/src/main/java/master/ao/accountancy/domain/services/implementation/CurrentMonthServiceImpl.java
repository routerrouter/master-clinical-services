package master.ao.accountancy.domain.services.implementation;

import lombok.RequiredArgsConstructor;
import master.ao.accountancy.domain.exceptions.CurrentMonthNotFoundException;
import master.ao.accountancy.domain.exceptions.ExistingDataException;
import master.ao.accountancy.domain.models.CurrentMonth;
import master.ao.accountancy.domain.repositories.CurrentMonthRepository;
import master.ao.accountancy.domain.services.CurrentMonthService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CurrentMonthServiceImpl implements CurrentMonthService {

    private final CurrentMonthRepository currentMonthRepository;

    @Override
    public CurrentMonth activeCurrentYear(UUID currentMonthId, CurrentMonth currentMonth) {
        validateYear(currentMonth);
        var currentYear = fetchOrFail(currentMonthId);
        currentYear.setMonth(currentMonth.getMonth());
        currentYear.setYear(currentMonth.getYear());
        return currentMonthRepository.save(currentYear);
    }


    @Override
    public CurrentMonth fetchOrFail(UUID currentYearId) {
        return currentMonthRepository.findById(currentYearId)
                .orElseThrow(() -> new CurrentMonthNotFoundException(currentYearId));
    }

    @Override
    public CurrentMonth getActiveYear() {
        return currentMonthRepository.findAll()
                .stream()
                .findFirst()
                .get();
    }

    @Override
    public void validateYear(CurrentMonth currentMonth) {
        var yearOptional = currentMonthRepository.findByYearAndMonth(currentMonth.getYear(), currentMonth.getMonth());
        if (yearOptional.isPresent())
            throw new ExistingDataException("Ano financeiro informado já está ativo!");
    }
}

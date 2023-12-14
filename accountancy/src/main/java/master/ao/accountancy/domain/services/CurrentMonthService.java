package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.CurrentMonth;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CurrentMonthService {
    void activeCurrentYear(CurrentMonth currentMonth);
    CurrentMonth fetchOrFail(UUID currentYearId);
    CurrentMonth getActiveYear();
    void validateYear(CurrentMonth currentMonth);
}

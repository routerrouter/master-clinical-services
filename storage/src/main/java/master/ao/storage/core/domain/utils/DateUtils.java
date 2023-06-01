package master.ao.authuser.core.domain.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class DateUtils {

    public long getDiferenceBetweenDatesIndDays(LocalDate initialDate, LocalDate finalDate) {
        return ChronoUnit.DAYS.between(initialDate, finalDate);
    }
}

package master.ao.accountancy.domain.repositories;

import master.ao.accountancy.domain.models.CurrentMonth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CurrentMonthRepository extends JpaRepository<CurrentMonth, UUID>  {
    Optional<CurrentMonth> findByYearAndMonth(int year, String month);
}

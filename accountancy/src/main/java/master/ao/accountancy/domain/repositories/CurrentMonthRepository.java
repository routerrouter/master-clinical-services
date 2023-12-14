package master.ao.accountancy.domain.repositories;

import master.ao.accountancy.domain.models.CurrentMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CurrentMonthRepository extends JpaRepository<CurrentMonth, UUID>  {
    Optional<CurrentMonth> findByYearAndMonth(int year, String month);

    @Modifying
    @Query("UPDATE  CurrentMonth  c set c.year=?1, c.month=?2 ")
    void updateCurrentPeriod(int year, String month);
}

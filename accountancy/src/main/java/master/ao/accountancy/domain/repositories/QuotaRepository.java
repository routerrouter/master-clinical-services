package master.ao.accountancy.domain.repositories;

import master.ao.accountancy.domain.models.Quota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuotaRepository extends JpaRepository<Quota, UUID>  {


    @Query("SELECT q FROM Quota  q WHERE  q.quotaYear=?1 and q.nature.natureId=?2 and q.quotaMonth=?3")
    Optional<Quota> findByYearAndNatureAndQuotaMonth(int year, UUID natureId, String month);

    @Query("SELECT q FROM Quota  q WHERE  q.nature.natureId=?1")
    List<Quota> findAllByNature(UUID natureId);
}

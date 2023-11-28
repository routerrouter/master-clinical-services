package master.ao.accountancy.domain.repositories;

import master.ao.accountancy.domain.models.ExpensePeople;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpensePeopleRepository extends JpaRepository<ExpensePeople, UUID>  {


    @Query("SELECT e FROM ExpensePeople  e WHERE  e.year=?1 and e.nature.natureId=?2")
    Optional<ExpensePeople> findByYearAndNature(int year, UUID natureId);


    @Query("SELECT e FROM ExpensePeople  e WHERE  e.nature.natureId=?1")
    List<ExpensePeople> findAllByNature(UUID natureId);

}

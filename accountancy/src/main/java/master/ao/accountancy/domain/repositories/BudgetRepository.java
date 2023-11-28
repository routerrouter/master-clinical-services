package master.ao.accountancy.domain.repositories;

import master.ao.accountancy.domain.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID>  {


    @Query("SELECT b FROM Budget  b WHERE  b.currentYear=?1 and b.nature.natureId=?2")
    Optional<Budget> findByYearAndNature(int year, UUID natureId);


    @Query("SELECT b FROM Budget  b WHERE  b.nature.natureId=?1")
    List<Budget> findAllByNature(UUID natureId);

}

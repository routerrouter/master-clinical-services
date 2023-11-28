package master.ao.accountancy.domain.repositories;

import master.ao.accountancy.domain.models.FinancialProgramming;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinancialProgrammingRepository extends JpaRepository<FinancialProgramming, UUID>  {


    @Query("SELECT p FROM FinancialProgramming  p WHERE  p.programmingYear=?1 and p.nature.natureId=?2")
    Optional<FinancialProgramming> verifyExistence(int year, UUID natureId);

    @Query("SELECT p FROM FinancialProgramming  p WHERE  p.programmingYear=?1 and p.nature.natureId=?3 and p.programmingMonth=?2 and p.subAccount.subAccountId=?4 ")
    Optional<FinancialProgramming> verifyExistenceByProvider(int year, String month, UUID natureId, UUID providerId);

    @Query("SELECT b FROM Budget  b WHERE  b.currentYear=?1 and b.nature.natureId=?2")
    Optional<FinancialProgramming> finallyVerify(int year, UUID natureId);

    @Query("SELECT b FROM Budget  b WHERE  b.currentYear=?1 and b.nature.natureId=?2")
    Optional<FinancialProgramming> validateDocument(int year, UUID natureId);


    @Query("SELECT b FROM Budget  b WHERE  b.nature.natureId=?1")
    List<FinancialProgramming> finallyVerify(UUID natureId);

}

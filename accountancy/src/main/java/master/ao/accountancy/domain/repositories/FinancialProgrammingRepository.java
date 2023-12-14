package master.ao.accountancy.domain.repositories;

import master.ao.accountancy.domain.models.FinancialProgramming;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinancialProgrammingRepository extends JpaRepository<FinancialProgramming, UUID>  {


    @Query("SELECT p FROM FinancialProgramming  p WHERE  p.programmingYear=?1 and p.nature.natureId=?2")
    Optional<FinancialProgramming> verifyExistence(int year, UUID natureId);

    @Query("SELECT p FROM FinancialProgramming  p WHERE  p.programmingYear=?1 and p.nature.natureId=?3 and p.programmingMonth=?2 and p.subAccount.subAccountId=?4 ")
    Optional<FinancialProgramming> verifyExistenceByProvider(int year, String month, UUID natureId, UUID providerId);

    @Query(value = "SELECT * FROM tb_programming  fp WHERE  fp.year=?1 and fp.month=?2 AND fp.finished='SIM' limit 1", nativeQuery = true)
    Optional<FinancialProgramming> finallyVerify(int year, String month);


    @Modifying
    @Query(value = "UPDATE  tb_programming  SET finished='SIM' WHERE  fp.year=?1 and fp.month=?2", nativeQuery = true)
    void finallyProgramming(int year, String month);

    @Query("SELECT fp FROM FinancialProgramming  fp WHERE  fp.programmingYear=?1 and fp.programmingMonth=?2")
    Optional<FinancialProgramming> validateDocument(int year, String month);


    @Query("SELECT fp FROM FinancialProgramming  fp WHERE  fp.nature.natureId=?1")
    List<FinancialProgramming> findAll(UUID natureId);

}

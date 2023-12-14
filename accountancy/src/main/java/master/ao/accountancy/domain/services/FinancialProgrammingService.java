package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.FinancialProgramming;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinancialProgrammingService {

    void create(List<FinancialProgramming> programming);
    void delete(UUID programmingId);
    void update(FinancialProgramming programming, UUID programmingId);
    void finallyProgramming();
    Optional<FinancialProgramming> fetchOrFail(UUID programmingId);
    List<FinancialProgramming> findAllProgramming(UUID natureId);
    void verifyExistence(int year, String month, UUID natureId, UUID providerId);
    void finallyVerify(int year, String month,String message);
    void verifyExistence(int year, String month);
    void linesVerifying();
    void validateDocument(int year, String month);
    List<FinancialProgramming> getAll(int year, String month);
    void updateValueAt(FinancialProgramming financialProgramming,UUID programmingId);

}

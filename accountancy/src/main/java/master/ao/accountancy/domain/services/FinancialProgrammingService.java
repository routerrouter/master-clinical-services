package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.FinancialProgramming;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinancialProgrammingService {

    FinancialProgramming save(FinancialProgramming programming);
    void delete(UUID programmingId);
    void update(UUID programmingId);
    FinancialProgramming finallyProgramming(int year, String month);
    List<FinancialProgramming> findAllProgramming();
    void verifyExistence(int year, String month, UUID natureId, UUID providerId);
    void finallyVerify(int year, String month);
    void verifyExistence(int year, String month);
    void linesVerifying();
    void validateDocument(int year, String month);
    List<FinancialProgramming> getAll(int year, String month);
    void updateValueAt(int document, String valueNcb,String valueLiq,String valueOs,UUID programmingId);

}

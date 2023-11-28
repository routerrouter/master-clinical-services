package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class BudgetNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public BudgetNotFoundException(String mensagem) {
        super(mensagem);
    }

    public BudgetNotFoundException(int budgetYear, UUID natureId) {
        this(String.format("Não existe um Orçamento de Abertura para natureza com o código: %s",natureId.toString()));
    }
    public BudgetNotFoundException(UUID budgetId) {
        this(String.format("Não existe um Orçamento de Abertura com o código: %s", budgetId.toString()));
    }

}
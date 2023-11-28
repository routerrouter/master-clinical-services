package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class ExpensePeopleNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public ExpensePeopleNotFoundException(String mensagem) {
        super(mensagem);
    }

    public ExpensePeopleNotFoundException(UUID budgetId) {
        this(String.format("Não existe um lançamento de despesa com pessoal para natureza com o código: %s", budgetId.toString()));
    }

}
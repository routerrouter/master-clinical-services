package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class FinancialProgrammingNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public FinancialProgrammingNotFoundException(String mensagem) {
        super(mensagem);
    }

    public FinancialProgrammingNotFoundException(UUID financialProgrammingId) {
        this(String.format("Não existe uma programação financeira com o código: %s", financialProgrammingId.toString()));
    }

}
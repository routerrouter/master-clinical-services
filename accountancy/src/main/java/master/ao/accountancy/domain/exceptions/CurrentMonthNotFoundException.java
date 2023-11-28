package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class CurrentMonthNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public CurrentMonthNotFoundException(String mensagem) {
        super(mensagem);
    }

    public CurrentMonthNotFoundException(UUID currentYearId) {
        this(String.format("Não existe um ano financeiro com código: %s", currentYearId.toString()));
    }

}
package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class AccountClassNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public AccountClassNotFoundException(String mensagem) {
        super(mensagem);
    }

    public AccountClassNotFoundException(UUID accountClassId) {
        this(String.format("Não existe um cadastro de classe de contas com código: %s", accountClassId.toString()));
    }

}
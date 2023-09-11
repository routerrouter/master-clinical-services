package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class AccountNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(String mensagem) {
        super(mensagem);
    }

    public AccountNotFoundException(UUID accountId) {
        this(String.format("Não existe um cadastro de conta com código: %s", accountId.toString()));
    }

}
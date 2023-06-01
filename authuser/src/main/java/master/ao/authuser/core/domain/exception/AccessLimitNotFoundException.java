package master.ao.authuser.core.domain.exception;

import java.util.UUID;

public class AccessLimitNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public AccessLimitNotFoundException(String mensagem) {
        super(mensagem);
    }

    public AccessLimitNotFoundException(UUID userId) {
        this(String.format("Não existe um cadastro de limite de acesso para o usuário com código %s", userId));
    }

}
package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class UserNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String mensagem) {
        super(mensagem);
    }

    public UserNotFoundException(UUID userId) {
        this(String.format("Não existe um cadastro de usuário com código: %s", userId.toString()));
    }

}
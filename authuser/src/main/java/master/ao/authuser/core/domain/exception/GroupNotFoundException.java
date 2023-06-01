package master.ao.authuser.core.domain.exception;

import java.util.UUID;

public class GroupNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public GroupNotFoundException(String mensagem) {
        super(mensagem);
    }

    public GroupNotFoundException(UUID groupId) {
        this(String.format("Não existe um cadastro de grupo com código: %s", groupId.toString()));
    }

}
package master.ao.storage.core.domain.exceptions;

import java.util.UUID;

public class TransferNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public TransferNotFoundException(String mensagem) {
        super(mensagem);
    }

    public TransferNotFoundException(UUID movementId) {
        this(String.format("Não existe um cadastro de transfer com código: %s", movementId.toString()));
    }

}
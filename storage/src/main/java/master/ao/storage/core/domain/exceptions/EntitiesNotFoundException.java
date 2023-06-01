package master.ao.storage.core.domain.exceptions;

import java.util.UUID;

public class EntitiesNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public EntitiesNotFoundException(String mensagem) {
        super(mensagem);
    }

    public EntitiesNotFoundException(UUID entityId) {
        this(String.format("Não existe um cadastro de entidade com código: %s", entityId.toString()));
    }

}
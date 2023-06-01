package master.ao.storage.core.domain.exceptions;

import java.util.UUID;

public class CategoryNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public CategoryNotFoundException(String mensagem) {
        super(mensagem);
    }

    public CategoryNotFoundException(UUID categoryId) {
        this(String.format("Não existe um cadastro de categoria com código: %s", categoryId.toString()));
    }

}
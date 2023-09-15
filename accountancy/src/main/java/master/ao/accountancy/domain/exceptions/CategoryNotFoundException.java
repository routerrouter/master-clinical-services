package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class CategoryNotFoundException extends EntityNotFoundException {

    public CategoryNotFoundException(String mensagem) {
        super(mensagem);
    }

    public CategoryNotFoundException(UUID categoryId) {
        this(String.format("Não existe um cadastro de categoria com código: %s", categoryId.toString()));
    }

}
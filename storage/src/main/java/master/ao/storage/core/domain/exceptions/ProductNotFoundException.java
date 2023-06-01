package master.ao.storage.core.domain.exceptions;

import java.util.UUID;

public class ProductNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(String mensagem) {
        super(mensagem);
    }

    public ProductNotFoundException(UUID productId) {
        this(String.format("Não existe um cadastro de produto com código: %s", productId.toString()));
    }

}
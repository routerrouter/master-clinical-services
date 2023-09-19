package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class DocumentNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public DocumentNotFoundException(String mensagem) {
        super(mensagem);
    }

    public DocumentNotFoundException(UUID documentId) {
        this(String.format("Não existe um cadastro de documento com código: %s", documentId.toString()));
    }

}
package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class NatureNotFoundException extends EntityNotFoundException {

    public NatureNotFoundException(String mensagem) {
        super(mensagem);
    }

    public NatureNotFoundException(UUID natureId) {
        this(String.format("Não existe um cadastro de natureza com código: %s", natureId.toString()));
    }

}
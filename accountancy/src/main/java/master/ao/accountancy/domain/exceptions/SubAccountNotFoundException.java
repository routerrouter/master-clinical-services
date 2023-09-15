package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class SubAccountNotFoundException extends EntityNotFoundException {

    public SubAccountNotFoundException(String mensagem) {
        super(mensagem);
    }

    public SubAccountNotFoundException(UUID subAccountId) {
        this(String.format("Não existe um cadastro de subconta com código: %s", subAccountId.toString()));
    }

}
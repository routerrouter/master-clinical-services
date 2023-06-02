package master.ao.storage.core.domain.exceptions;

import java.util.UUID;

public class CompanyNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public CompanyNotFoundException(String mensagem) {
        super(mensagem);
    }

    public CompanyNotFoundException(UUID companyId) {
        this(String.format("Não existe um cadastro de instituição com código: %s", companyId.toString()));
    }

}
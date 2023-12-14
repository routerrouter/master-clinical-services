package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class InvoiceNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public InvoiceNotFoundException(String mensagem) {
        super(mensagem);
    }

    public InvoiceNotFoundException() {
        this(String.format("Já existe uma factura deste fornecedor com este número"));
    }
    public InvoiceNotFoundException(UUID quotaId) {
        this(String.format("Não existe uma Fatura com o código: %s", quotaId.toString()));
    }

}
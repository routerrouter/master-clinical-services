package master.ao.accountancy.domain.exceptions;

import java.util.UUID;

public class QuotaNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public QuotaNotFoundException(String mensagem) {
        super(mensagem);
    }

    public QuotaNotFoundException(int quotaYear, UUID natureId) {
        this(String.format("Não existe uma Quota Mensal para o ano de: %s", String.valueOf(quotaYear)," e a natureza de código: %s",  natureId.toString()));
    }
    public QuotaNotFoundException(UUID quotaId) {
        this(String.format("Não existe uma Quota Mensal com o código: %s", quotaId.toString()));
    }

}
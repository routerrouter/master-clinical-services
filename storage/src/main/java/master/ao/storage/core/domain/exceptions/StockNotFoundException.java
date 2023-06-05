package master.ao.storage.core.domain.exceptions;

import java.time.LocalDate;
import java.util.UUID;

public class StockNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public StockNotFoundException(String mensagem) {
        super(mensagem);
    }

    public StockNotFoundException() {
        this(String.format("Não existe estocagem produto com estas caracteristicas {} "));
    }
    public StockNotFoundException(UUID storageId, UUID productId, String loteOrModel, LocalDate expiration) {
        this(String.format("Não existe estocagem produto com estas caracteristicas {} ",loteOrModel));
    }

}
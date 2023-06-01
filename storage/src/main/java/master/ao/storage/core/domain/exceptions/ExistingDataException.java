package master.ao.storage.core.domain.exceptions;

public class ExistingDataException extends BussinessException {

    private static final long serialVersionUID = 1L;

    public ExistingDataException(String mensagem) {
        super(mensagem);
    }

}
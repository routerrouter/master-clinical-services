package master.ao.accountancy.domain.exceptions;


public class BussinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BussinessException(String mensagem) {
        super(mensagem);
    }

    public BussinessException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }

}
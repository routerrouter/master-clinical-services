package master.ao.authuser.core.domain.exception;

public class BadCredentialsException extends BussinessException {

    private static final long serialVersionUID = 1L;

    public BadCredentialsException(String mensagem) {
        super(mensagem);
    }
}

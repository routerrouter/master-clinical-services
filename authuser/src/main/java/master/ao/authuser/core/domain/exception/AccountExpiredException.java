package master.ao.authuser.core.domain.exception;

public class AccountExpiredException extends BussinessException {

    private static final long serialVersionUID = 1L;

    public AccountExpiredException(String mensagem) {
        super(mensagem);
    }


}

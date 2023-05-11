package master.ao.authuser.core.domain.exception;

public class AccountLockedException extends BussinessException {

    private static final long serialVersionUID = 1L;

    public AccountLockedException(String mensagem) {
        super(mensagem);
    }


}

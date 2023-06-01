package master.ao.authuser.core.domain.exception;

public class AccountAccessLimitException extends BussinessException {

    private static final long serialVersionUID = 1L;

    public AccountAccessLimitException(String mensagem) {
        super(mensagem);
    }


}

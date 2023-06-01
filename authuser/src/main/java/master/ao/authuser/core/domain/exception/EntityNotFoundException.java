package master.ao.authuser.core.domain.exception;


public abstract class EntityNotFoundException extends BussinessException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String mensagem) {
        super(mensagem);
    }

}
package master.ao.accountancy.domain.exceptions;


public class EntityInUseException extends BussinessException {

    private static final long serialVersionUID = 1L;

    public EntityInUseException(String mensagem) {
        super(mensagem);
    }

}
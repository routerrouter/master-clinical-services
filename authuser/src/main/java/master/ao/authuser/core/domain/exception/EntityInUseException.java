package master.ao.authuser.core.domain.exception;


public class EntityInUseException extends BussinessException {

	private static final long serialVersionUID = 1L;

	public EntityInUseException(String mensagem) {
		super(mensagem);
	}
	
}
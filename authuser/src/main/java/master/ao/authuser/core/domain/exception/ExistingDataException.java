package master.ao.authuser.core.domain.exception;

public class ExistingDataException extends BussinessException {

	private static final long serialVersionUID = 1L;

	public ExistingDataException(String mensagem) {
		super(mensagem);
	}
	
}
package master.ao.authuser.core.domain.exception;


public class BussinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BussinessException(String mensagem) {
		super(mensagem);
	}
	
	public BussinessException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
	
}
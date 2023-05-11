package master.ao.authuser.core.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String mensagem) {
		super(mensagem);
	}
	
	public UserNotFoundException(UUID id) {
		this(String.format("Não existe um cadastro de usuário com código %s", id));
	}
	
}
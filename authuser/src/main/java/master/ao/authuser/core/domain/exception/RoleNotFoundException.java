package master.ao.authuser.core.domain.exception;

import java.util.UUID;

public class RoleNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public RoleNotFoundException(String mensagem) {
		super(mensagem);
	}
	
	public RoleNotFoundException(UUID estadoId) {
		this(String.format("Não existe um cadastro de grupo com código %s", estadoId));
	}
	
}
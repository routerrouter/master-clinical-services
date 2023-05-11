package master.ao.authuser.core.domain.exception;

import java.util.UUID;

public class PermissionNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public PermissionNotFoundException(String mensagem) {
		super(mensagem);
	}
	
	public PermissionNotFoundException(UUID permissaoId) {
		this(String.format("Não existe um cadastro de permissão com código %s", permissaoId));
	}
	
}
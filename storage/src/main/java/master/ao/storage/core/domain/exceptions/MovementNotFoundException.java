package master.ao.storage.core.domain.exceptions;

import java.util.UUID;

public class MovementNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public MovementNotFoundException(String mensagem) {
		super(mensagem);
	}

	public MovementNotFoundException(UUID movementId) {
		this(String.format("Não existe um cadastro de movimento com código: %s", movementId.toString()));
	}
	
}
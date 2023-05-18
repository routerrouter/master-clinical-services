package master.ao.storage.core.domain.exceptions;

import java.util.UUID;

public class NatureNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public NatureNotFoundException(String mensagem) {
		super(mensagem);
	}

	public NatureNotFoundException(UUID natureId) {
		this(String.format("Não existe um cadastro de natureza com código: %s", natureId.toString()));
	}
	
}
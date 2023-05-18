package master.ao.storage.core.domain.exceptions;

import java.util.UUID;

public class LocationNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public LocationNotFoundException(String mensagem) {
		super(mensagem);
	}

	public LocationNotFoundException(UUID locationId) {
		this(String.format("Não existe um cadastro de localização com código: %s", locationId.toString()));
	}
	
}
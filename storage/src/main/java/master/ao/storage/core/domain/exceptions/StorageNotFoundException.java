package master.ao.storage.core.domain.exceptions;

import java.util.UUID;

public class StorageNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public StorageNotFoundException(String mensagem) {
		super(mensagem);
	}

	public StorageNotFoundException(UUID storageId) {
		this(String.format("Não existe um cadastro de armazém com código: %s", storageId.toString()));
	}
	
}
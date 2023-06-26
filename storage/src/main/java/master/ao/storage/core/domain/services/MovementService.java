package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.Movement;

import java.util.List;
import java.util.UUID;

public interface MovementService {
    Movement save(Movement movement, UUID userId,UUID originStorageId);
    List<Movement> listAll();

}

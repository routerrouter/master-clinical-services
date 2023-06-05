package master.ao.authuser.core.domain.service;

import master.ao.authuser.core.domain.model.Storage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageService {
    void save(Storage Storage);
    void delete(UUID storageId);
    List<Storage> findAll(UUID userGroup);
}

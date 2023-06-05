package master.ao.authuser.core.domain.service.impl;

import lombok.RequiredArgsConstructor;
import master.ao.authuser.core.domain.model.Storage;
import master.ao.authuser.core.domain.repositories.StorageRepository;
import master.ao.authuser.core.domain.service.StorageService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;

    @Transactional
    @Override
    public void save(Storage storage) {
        var storageOptional = storageRepository.findById(storage.getStorageId());
        if (storageOptional.isPresent()) {
            storageRepository.updateStorage(storage.getStorageId(), storage.getName(), storage.getUserGroup());
        } else {
            storageRepository.saveStorage(storage.getStorageId(), storage.getName(), storage.getUserGroup());
        }
    }


    @Override
    public void delete(UUID storageId) {
        storageRepository.deleteById(storageId);
    }

    @Transactional
    @Override
    public List<Storage> findAll(UUID userGroup) {
        return storageRepository.findStorageByUserGroup(userGroup);
    }
}
package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.clients.StorageClient;
import master.ao.storage.api.mapper.StorageMapper;
import master.ao.storage.core.domain.exceptions.*;
import master.ao.storage.core.domain.models.Storage;
import master.ao.storage.core.domain.repositories.StorageRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.StorageService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private static final String MSG_STORAGE_IN_USE
            = "Armazém não pode ser removido, pois está em uso";


    private final StorageRepository storageRepository;
    private final UserRepository userRepository;
    private final StorageClient storageClient;
    private final StorageMapper mapper;

    @Override
    @Transactional
    public Storage save(Storage storage, UUID userId, String token) {
        var storageOptional = storageRepository.findByName(storage.getName());
        if (storageOptional.isPresent()) {
            throw new ExistingDataException("Armazém informado já existe.");
        }
        var userGroup = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        storage.setUserGroup(userGroup.getGroupId());
        var storageSaved = storageRepository.saveAndFlush(storage);
        saveOrUpdateStorageToAuthuser(storageSaved,token);
        return storageSaved;
    }

    @Override
    @Transactional
    public Storage update(Storage storage, UUID storageId, String token) {
        var storageOptional = fetchOrFail(storageId).get();
        storageOptional.setName(storage.getName());
        storageOptional.setDescription(storage.getDescription());
        storageOptional.setCapacity(storage.getCapacity());
        storageOptional.setLastUpdateAt(LocalDateTime.now(ZoneId.of("UTC")));
        storageOptional = storageRepository.saveAndFlush(storageOptional);

        saveOrUpdateStorageToAuthuser(storageOptional, token);

        return storageOptional;
    }

    @Override
    @Transactional
    public void disabledOrEnabled(UUID storageId) {

        try {
            var groupOptional = fetchOrFail(storageId).get();
            storageRepository.delete(groupOptional);
            storageRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new StorageNotFoundException(storageId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_STORAGE_IN_USE);
        }

    }


    @Override
    @Transactional
    public void delete(UUID storageId, String token) {
        try {
            var storageOptional = fetchOrFail(storageId).get();
            storageRepository.delete(storageOptional);
            storageRepository.flush();

            storageClient.deleteStorageToAuthuser(storageId,token);

        } catch (EmptyResultDataAccessException e) {
            throw new StorageNotFoundException(storageId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_STORAGE_IN_USE);
        }
    }

    @Override
    public Optional<Storage> fetchOrFail(UUID storageId) {
        var nature = storageRepository.findById(storageId)
                .orElseThrow(() -> new StorageNotFoundException(storageId));

        return Optional.of(nature);
    }


    @Override
    public List<Storage> findAll(Specification<Storage> spec) {
        return storageRepository.findAll(spec);
    }


    private void saveOrUpdateStorageToAuthuser(Storage storage, String token) {
        storageClient.saveStorageToAuthuser(storage, token);
    }


}
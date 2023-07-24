package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.clients.StorageClient;
import master.ao.storage.api.config.security.AuthenticationCurrentUserService;
import master.ao.storage.api.config.security.UserDetailsImpl;
import master.ao.storage.api.mapper.StorageMapper;
import master.ao.storage.core.domain.exceptions.*;
import master.ao.storage.core.domain.models.Storage;
import master.ao.storage.core.domain.models.User;
import master.ao.storage.core.domain.repositories.StorageRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.StorageService;
import master.ao.storage.core.domain.services.UtilService;
import master.ao.storage.core.domain.utils.Converts;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.Convert;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private static final String MSG_STORAGE_IN_USE
            = "Armazém não pode ser removido, pois está em uso";


    private final StorageRepository storageRepository;
    private final StorageClient storageClient;
    private final Converts convert;
    private final UtilService utilService;

    @Override
    @Transactional
    public Storage save(Storage storage, String token) {
        var storageOptional = storageRepository.findByName(storage.getName());
        if (storageOptional.isPresent()) {
            throw new ExistingDataException("Armazém informado já existe.");
        }

        storage.setUserGroup(utilService.getUserGroup());
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

        return storageRepository.findAll(spec)
                .stream()
                .filter(storage -> convert.convertUuidToString(storage.getUserGroup())
                        .equals(convert.convertUuidToString(utilService.getUserGroup())))
                .collect(Collectors.toList());
    }


    private void saveOrUpdateStorageToAuthuser(Storage storage, String token) {
        storageClient.saveStorageToAuthuser(storage, token);
    }


}
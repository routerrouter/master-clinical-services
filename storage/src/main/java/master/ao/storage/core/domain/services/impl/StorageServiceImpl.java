package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.exceptions.EntityInUseException;
import master.ao.storage.core.domain.exceptions.NatureNotFoundException;
import master.ao.storage.core.domain.models.Nature;
import master.ao.storage.core.domain.models.Storage;
import master.ao.storage.core.domain.repositories.StorageRepository;
import master.ao.storage.core.domain.services.StorageService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private static final String MSG_NATURE_IN_USE
            = "Grupo não pode ser removido, pois está em uso";


    private final StorageRepository storageRepository;

    @Override
    @Transactional
    public Storage save(Storage storage) {
        var storageOptional = storageRepository.findByName(storage.getName());
        if (storageOptional.isPresent()) {
            throw new BussinessException("Armazém informado já existe.");
        }

        return storageRepository.save(storage);
    }

    @Override
    public Storage update(Storage nature, UUID storageId) {
        var storageOptional = fetchOrFail(storageId).get();
        storageOptional.setName(nature.getName());

        return storageRepository.save(storageOptional);
    }

    @Override
    @Transactional
    public void disabledOrEnabled(UUID storageId) {

        var natureOptional = fetchOrFail(storageId).get();
       // implementar a lógica....

    }


    @Override
    @Transactional
    public void delete(UUID storageId) {
        try {
            var natureOptional = fetchOrFail(storageId).get();
            storageRepository.delete(natureOptional);
            storageRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new NatureNotFoundException(storageId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_NATURE_IN_USE);
        }
    }

    @Override
    public Optional<Storage> fetchOrFail(UUID storageId) {
        var nature = storageRepository.findById(storageId)
                .orElseThrow(() -> new NatureNotFoundException(storageId));

        return Optional.of(nature);
    }


    @Override
    public List<Storage> findAll(Specification<Storage> spec) {
        return storageRepository.findAll(spec);
    }

}
package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.exceptions.*;
import master.ao.storage.core.domain.models.Entities;
import master.ao.storage.core.domain.repositories.EntitiesRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.EntityService;
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
public class EntityServiceImpl implements EntityService {

    private static final String MSG_ENTITIES_IN_USE
            = "Entidade não pode ser removida, pois está em uso";

    private final EntitiesRepository repository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Entities saveEndCreateAccount(Entities entity, UUID userId) {
        var userGroup = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        var entityOptional = repository.findByName(entity.getName());
        if (entityOptional.isPresent()) {
            throw new ExistingDataException("Nome de entidade informado já existe!");
        }
        entity.setUserGroup(userGroup.getGroupId());
        entity.setEnabled(true);
        entity.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        Entities entitySaved = repository.save(entity);
        if (entitySaved.getEntityType().equals("SUPPLIER")) {
            // createAccount(entitySaved);
        }
        return  entitySaved;
    }

    @Transactional
    @Override
    public Entities updateAndUpdateAccount(Entities entity, UUID entityId) {
        var entityOptional = fetchOrFail(entityId).get();
        entityOptional.setName(entity.getName());
        entityOptional.setAddress(entity.getAddress());
        entityOptional.setEmail(entity.getEmail());
        entityOptional.setNif(entity.getNif());
        entityOptional.setPhoneNumber(entity.getPhoneNumber());
        entityOptional.setResponsible(entity.getResponsible());
        entityOptional.setEntityType(entity.getEntityType());
        entityOptional.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return repository.save(entityOptional);
    }

    @Override
    public void delete(UUID entityId) {
        try {
            var entityOptional = fetchOrFail(entityId).get();
            repository.delete(entityOptional);
            repository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new EntitiesNotFoundException(entityId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_ENTITIES_IN_USE);
        }
    }

    @Override
    public Optional<Entities> fetchOrFail(UUID entityId) {
        var entityOptional = repository.findById(entityId)
                .orElseThrow(() -> new  EntitiesNotFoundException(entityId));
        return Optional.of(entityOptional);
    }

    @Override
    public List<Entities> findAll(Specification<Entities> spec) {
        return repository.findAll(spec);
    }
}

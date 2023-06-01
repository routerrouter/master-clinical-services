package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.exceptions.EntityInUseException;
import master.ao.storage.core.domain.exceptions.ExistingDataException;
import master.ao.storage.core.domain.exceptions.NatureNotFoundException;
import master.ao.storage.core.domain.models.Nature;
import master.ao.storage.core.domain.repositories.NatureRepository;
import master.ao.storage.core.domain.services.NatureService;
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
public class NatureServiceImpl implements NatureService {

    private static final String MSG_NATURE_IN_USE
            = "Natureza não pode ser removido, pois está em uso";


    private final NatureRepository natureRepository;

    @Override
    @Transactional
    public Nature save(Nature nature, UUID userId) {
        var natureOptional = natureRepository.findByName(nature.getName());
        if (natureOptional.isPresent()) {
            throw new ExistingDataException("Natureza informada já existe.");
        }

        return natureRepository.save(nature);
    }

    @Override
    public Nature update(Nature nature, UUID natureId) {
        var natureOptional = fetchOrFail(natureId).get();
        natureOptional.setName(nature.getName());

        return natureRepository.save(natureOptional);
    }

    @Override
    @Transactional
    public void delete(UUID natureId) {
        try {
            var natureOptional = fetchOrFail(natureId).get();
            natureRepository.delete(natureOptional);
            natureRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new NatureNotFoundException(natureId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_NATURE_IN_USE);
        }
    }

    @Override
    public Optional<Nature> fetchOrFail(UUID natureId) {
        var nature = natureRepository.findById(natureId)
                .orElseThrow(() -> new NatureNotFoundException(natureId));

        return Optional.of(nature);
    }


    @Override
    public List<Nature> findAll(Specification<Nature> spec) {
        return natureRepository.findAll(spec);
    }

}
package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.exceptions.EntityInUseException;
import master.ao.storage.core.domain.exceptions.ExistingDataException;
import master.ao.storage.core.domain.exceptions.LocationNotFoundException;
import master.ao.storage.core.domain.models.Location;
import master.ao.storage.core.domain.repositories.LocationRepository;
import master.ao.storage.core.domain.services.LocationService;
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
public class LocationServiceImpl implements LocationService {

    private static final String MSG_LOCATION_IN_USE
            = "Localização fisica não pode ser removido, pois está em uso";


    private final LocationRepository locationRepository;
    private final StorageService storageService;

    @Override
    @Transactional
    public Location save(Location location, UUID storageId) {
        var locationOptional = locationRepository.findByShelfAndPartition(location.getShelf(), location.getPartition());
        if (locationOptional.isPresent()) {
            throw new ExistingDataException("Localização informada já existe.");
        }
        var storage = storageService.fetchOrFail(storageId);
        location.setEnabeld(true);
        location.setStorage(storage.get());
        location.setRegisteredAt(LocalDateTime.now(ZoneId.of("UTC")));
        location.setDescription(location.setDescriptionLocation());
        return locationRepository.save(location);
    }

    @Override
    public Location update(Location location, UUID locationId) {
        var locationOptional = fetchOrFail(locationId).get();
        var storage = storageService.fetchOrFail(location.getStorage().getStorageId());
        locationOptional.setStorage(storage.get());
        locationOptional.setShelf(location.getShelf());
        locationOptional.setPartition(location.getPartition());
        locationOptional.setDescription(locationOptional.setDescriptionLocation());
        locationOptional.setLastUpdateAt(LocalDateTime.now(ZoneId.of("UTC")));


        return locationRepository.save(locationOptional);
    }

    @Override
    @Transactional
    public void delete(UUID locationId) {
        try {
            var locationOptional = fetchOrFail(locationId).get();
            locationRepository.delete(locationOptional);
            locationRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new LocationNotFoundException(locationId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_LOCATION_IN_USE);
        }
    }

    @Override
    public Optional<Location> fetchOrFail(UUID locationId) {
        var location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(locationId));

        return Optional.of(location);
    }


    @Override
    public List<Location> findAll(Specification<Location> spec) {
        return locationRepository.findAll(spec);
    }

}
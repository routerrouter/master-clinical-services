package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.exceptions.*;
import master.ao.storage.core.domain.models.Group;
import master.ao.storage.core.domain.repositories.GroupRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.GroupService;
import master.ao.storage.core.domain.services.UtilService;
import master.ao.storage.core.domain.utils.Converts;
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
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private static final String MSG_GROUP_IN_USE
            = "Grupo não pode ser removido, pois está em uso";


    private final GroupRepository groupRepository;
    private final Converts convert;
    private final UtilService utilService;

    @Override
    @Transactional
    public Group save(Group group, UUID userId) {
        var groupOptional = groupRepository.findByName(group.getName());
        if (groupOptional.isPresent()) {
            throw new ExistingDataException("Grupo informado já existe.");
        }
        group.setUserGroup(utilService.getUserGroup());
        return groupRepository.save(group);
    }

    @Override
    public Group update(Group group, UUID groupId) {
        var groupOptional = fetchOrFail(groupId).get();
        groupOptional.setName(group.getName());
        groupOptional.setLastUpdateAt(LocalDateTime.now(ZoneId.of("UTC")));

        return groupRepository.save(groupOptional);
    }

    @Override
    @Transactional
    public void delete(UUID groupId) {
        try {
            var groupOptional = fetchOrFail(groupId).get();
            groupRepository.delete(groupOptional);
            groupRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new GroupNotFoundException(groupId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_GROUP_IN_USE);
        }
    }

    @Override
    public Optional<Group> fetchOrFail(UUID groupId) {
        var group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        return Optional.of(group);
    }


    @Override
    public List<Group> findAll(Specification<Group> spec) {
        return groupRepository.findAll(spec)
                .stream()
                .filter(group -> convert.convertUuidToString(group.getUserGroup())
                .equals(convert.convertUuidToString(utilService.getUserGroup())))
                .collect(Collectors.toList());
    }

}
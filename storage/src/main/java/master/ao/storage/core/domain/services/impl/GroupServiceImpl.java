package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.exceptions.EntityInUseException;
import master.ao.storage.core.domain.exceptions.GroupNotFoundException;
import master.ao.storage.core.domain.models.Group;
import master.ao.storage.core.domain.repositories.GroupRepository;
import master.ao.storage.core.domain.services.GroupService;
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
public class GroupServiceImpl implements GroupService {

    private static final String MSG_GROUP_IN_USE
            = "Grupo não pode ser removido, pois está em uso";


    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public Group save(Group group) {
        var groupOptional = groupRepository.findByName(group.getName());
        if (groupOptional.isPresent()) {
            throw new BussinessException("Grupo informado já existe.");
        }
        return groupRepository.save(group);
    }

    @Override
    public Group update(Group group, UUID groupId) {
        var groupOptional = fetchOrFail(groupId).get();
        groupOptional.setName(group.getName());

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
        return groupRepository.findAll(spec);
    }

}
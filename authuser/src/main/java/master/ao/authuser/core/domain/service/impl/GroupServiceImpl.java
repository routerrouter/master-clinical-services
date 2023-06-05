package master.ao.authuser.core.domain.service.impl;

import lombok.RequiredArgsConstructor;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.exception.EntityInUseException;
import master.ao.authuser.core.domain.exception.GroupNotFoundException;
import master.ao.authuser.core.domain.model.Group;
import master.ao.authuser.core.domain.repositories.GroupRepository;
import master.ao.authuser.core.domain.service.GroupService;
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
        var groupOptional = groupRepository.findByDescription(group.getDescription());
        if (groupOptional.isPresent()) {
            throw new BussinessException("Grupo informado já existe.");
        }

        return groupRepository.save(group);
    }

    @Override
    public Group update(Group group, UUID groupId) {
        var groupOptional = fetchOrFail(groupId).get();
        groupOptional.setDescription(group.getDescription());

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
    @Transactional
    public void disassociatePermission(UUID groupId, List<UUID> permissions) {
        var group = fetchOrFail(groupId).get();

        permissions.forEach(permissionId -> {
            if (groupRepository.existGroupPermission(group.getGroupId(), permissionId) > 0L) {
                groupRepository.desassociatePermission(group.getGroupId(), permissionId);
            }
        });
    }

    @Override
    @Transactional
    public void associatePermissions(UUID groupId, List<UUID> permissions) {
        var group = fetchOrFail(groupId).get();

        permissions.forEach(permissionId -> {
            if (groupRepository.existGroupPermission(group.getGroupId(), permissionId) == 0L) {
                groupRepository.associatePermissions(group.getGroupId(), permissionId);
            }
        });

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

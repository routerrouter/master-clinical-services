package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID>, JpaSpecificationExecutor<Group> {

    Optional<Group> findByName(String name);

    Optional<Group> findById(UUID groupId);

}
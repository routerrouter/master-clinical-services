package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.Transfer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID>, JpaSpecificationExecutor<Transfer> {
    @Query(value = "Select t From Transfer t where t.storage.storageId=?1 ")
    List<Transfer> findAll(UUID storageId);
}
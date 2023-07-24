package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.ItemsTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemsTransferRepository extends JpaRepository<ItemsTransfer, UUID> {

    @Query("select items From ItemsTransfer  items where  items.transfer.transferId = ?1")
    List<ItemsTransfer> findAllByTransferId(@Param("transferId") UUID transferId);
}

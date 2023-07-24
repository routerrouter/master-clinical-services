package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.ItemsTransfer;
import master.ao.storage.core.domain.models.Transfer;

import java.util.List;
import java.util.UUID;

public interface TransferService {
    Transfer saveTransfer(Transfer transfer, UUID userId);
    List<Transfer> listByStorage(UUID storageId);
    List<ItemsTransfer> listItemsByTransfer(UUID transferId);
}

package master.ao.storage.core.domain.services;

import java.util.UUID;

public interface UtilService {
    String createUrlToAuthUser();
    String deleteStorageToAuthuser(UUID storageId);
}

package master.ao.storage.core.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UtilService {
    String createUrlToAuthUser();
    String deleteStorageToAuthuser(UUID storageId);
    UUID getUserGroup();
    ResponseEntity<Page<Object>> getPageResponseEntity(Pageable pageable, List<Object> objectList);
}

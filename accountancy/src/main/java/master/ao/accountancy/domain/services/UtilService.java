package master.ao.accountancy.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UtilService {
    String createUrlToAuthUser();
    ResponseEntity<Page<Object>> getPageResponseEntity(Pageable pageable, List<Object> objectList);
}

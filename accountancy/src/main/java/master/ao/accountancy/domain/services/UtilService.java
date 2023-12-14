package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.CurrentMonth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UtilService {
    String createUrlToAuthUser();
    UUID getUserGroup();
    CurrentMonth getCurrentPeriod();
    ResponseEntity<Page<Object>> getPageResponseEntity(Pageable pageable, List<Object> objectList);
}

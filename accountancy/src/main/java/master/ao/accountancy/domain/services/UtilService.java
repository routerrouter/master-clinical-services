package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.CurrentMonth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UtilService {
    CurrentMonth getCurrentPeriod();
    String createUrlToAuthUser();
    ResponseEntity<Page<Object>> getPageResponseEntity(Pageable pageable, List<Object> objectList);
}

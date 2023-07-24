package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.config.security.AuthenticationCurrentUserService;
import master.ao.storage.api.config.security.UserDetailsImpl;
import master.ao.storage.api.response.StockResponse;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.UtilService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UtilServiceImpl implements UtilService {

    String baseUrl = "/master-authuser/storage";

    private final AuthenticationCurrentUserService currentUserService;
    private final UserRepository userRepository;

    @Override
    public String createUrlToAuthUser() {
        return baseUrl;
    }

    @Override
    public String deleteStorageToAuthuser(UUID storageId) {
        return baseUrl.concat("/") + storageId;
    }

    @Override
    public UUID getUserGroup() {
        UserDetailsImpl userDetails = currentUserService.getCurrentUser();

        return userRepository
                .findById(userDetails.getUserId())
                .get()
                .getGroupId();

    }


    @Override
    public ResponseEntity<Page<Object>> getPageResponseEntity(Pageable pageable, List<Object> objectList) {
        int start = (int) (pageable.getOffset() > objectList.size() ? objectList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > objectList.size() ? objectList.size()
                : (start + pageable.getPageSize()));
        Page<Object> objectPage = new PageImpl<>(objectList.subList(start, end), pageable, objectList.size());

        return ResponseEntity.status(HttpStatus.OK).body(objectPage);
    }
}
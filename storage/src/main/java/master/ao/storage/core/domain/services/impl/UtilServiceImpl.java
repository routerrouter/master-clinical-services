package master.ao.storage.core.domain.services.impl;

import master.ao.storage.core.domain.services.UtilService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilServiceImpl implements UtilService {

    String baseUrl = "/master-authuser/storage";
    @Override
    public String createUrlToAuthUser() {
        return baseUrl;
    }

    @Override
    public String deleteStorageToAuthuser(UUID storageId) {
        return baseUrl.concat("/")+storageId;
    }
}
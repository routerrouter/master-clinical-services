package master.ao.authuser.core.domain.service.impl;

import master.ao.authuser.core.domain.service.UtilService;
import org.springframework.stereotype.Service;

@Service
public class UtilServiceImpl implements UtilService {

    @Override
    public String createUrlToStorage() {
        return "/master-storage/user";
    }
}

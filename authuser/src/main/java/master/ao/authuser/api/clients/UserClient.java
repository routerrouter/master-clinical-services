package master.ao.authuser.api.clients;

import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.request.UserOtherServicesRequest;
import master.ao.authuser.core.domain.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
@Log4j2
public class UserClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilService utilService;

    @Value("${master.api.url.storage}")
    String REQUEST_URL_STORAGE;

    @Value("${master.api.url.accountancy}")
    String REQUEST_URL_ACCOUNTANCY;

    public void saveUserToStorageAndOuther(UserOtherServicesRequest user, String token) {
        String urlStorage = REQUEST_URL_STORAGE+utilService.createUrlToStorage();
        String urlAccountancy = REQUEST_URL_ACCOUNTANCY+utilService.createUrlToAccountancy();

        log.debug("Request URL_STORAGE: {} ", urlStorage);
        log.info("Request URL_STORAGE: {} ", urlStorage);

        log.debug("Request URL_ACCOUNTANCY: {} ", urlAccountancy);
        log.info("Request URL_ACCOUNTANCY: {} ", urlAccountancy);

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);
            HttpEntity<UserOtherServicesRequest> request = new HttpEntity<UserOtherServicesRequest>(user, headers);

            restTemplate.postForObject(urlStorage, request, UserOtherServicesRequest.class);

            restTemplate.postForObject(urlAccountancy, request, UserOtherServicesRequest.class);

        } catch (HttpStatusCodeException e) {
            log.error("Error request /user/storage {} ", e);
            log.error("Error request /user/accountancy {} ", e);
        }

    }
}

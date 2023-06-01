package master.ao.authuser.api.clients;

import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.request.UserStorageRequest;
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

    public void saveUserToStorageAndOuther(UserStorageRequest user, String token) {
        String url = REQUEST_URL_STORAGE+utilService.createUrlToStorage();
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);
            HttpEntity<UserStorageRequest> request = new HttpEntity<UserStorageRequest>(user, headers);
            restTemplate.postForObject(url, request, UserStorageRequest.class);

        } catch (HttpStatusCodeException e) {
            log.error("Error request /user/storage {} ", e);
        }

    }
}

package master.ao.accountancy.api.clients;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
@Log4j2
public class AccountancyClient {

    @Autowired
    RestTemplate restTemplate;

    // @Autowired UtilService utilService;

    @Value("${master.api.url.authuser}")
    String REQUEST_URL_AUTHUSER;

    public void saveStorageToAuthuser(String token) {
        /*String url = REQUEST_URL_AUTHUSER+utilService.createUrlToAuthUser();
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);
            HttpEntity<Storage> request = new HttpEntity<Storage>(storage, headers);
            restTemplate.postForEntity(url, request, Storage.class);

        } catch (HttpStatusCodeException e) {
            log.error("Error request /storage {} ", e);
        }
*/
    }

    public void deleteStorageToAuthuser(UUID storageId, String token) {
        /*String url = REQUEST_URL_AUTHUSER+utilService.deleteStorageToAuthuser(storageId);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
        restTemplate.exchange(url,HttpMethod.DELETE,requestEntity,String.class);*/

    }

}

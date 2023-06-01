package master.ao.storage.api.clients;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Log4j2
public class StorageClient {

    @Autowired
    RestTemplate restTemplate;

}

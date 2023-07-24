package master.ao.storage.core.domain.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
public class Converts {

    public String convertUuidToString(UUID value) {
        return String.valueOf(value);
    }
}

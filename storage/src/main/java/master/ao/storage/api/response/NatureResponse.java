package master.ao.storage.api.response;

import lombok.Data;

import java.util.UUID;

@Data
public class NatureResponse {
    private UUID natureId;
    private String name;
}
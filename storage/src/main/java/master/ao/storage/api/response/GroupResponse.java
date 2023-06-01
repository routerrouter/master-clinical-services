package master.ao.storage.api.response;

import lombok.Data;

import java.util.UUID;

@Data
public class GroupResponse {
    private UUID groupId;
    private String name;
}
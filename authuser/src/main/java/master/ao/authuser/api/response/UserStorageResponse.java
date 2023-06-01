package master.ao.authuser.api.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserStorageResponse {

    private UUID userId;
    private String fullName;
    private String email;
    private String username;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
    private UUID groupId;
}

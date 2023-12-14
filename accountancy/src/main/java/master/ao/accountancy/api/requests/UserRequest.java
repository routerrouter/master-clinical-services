package master.ao.accountancy.api.requests;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserRequest {
    private UUID userId;
    private String fullName;
    private String email;
    private String username;
    private UUID groupId;
}

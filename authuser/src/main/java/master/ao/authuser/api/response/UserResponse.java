package master.ao.authuser.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import master.ao.authuser.core.domain.enums.UserStatus;
import master.ao.authuser.core.domain.model.Group;
import master.ao.authuser.core.domain.model.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private boolean enabled;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;
    private Group group;

}
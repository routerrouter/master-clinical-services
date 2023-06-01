package master.ao.authuser.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import master.ao.authuser.api.response.UserResponse;
import master.ao.authuser.core.domain.model.User;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class LoginResponseDetail {

    @NonNull
    private String token;
    private String type = "Bearer";
    private UserResponse user;
    List<Object> accsses;

}
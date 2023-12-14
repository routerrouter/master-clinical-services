package master.ao.accountancy.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.accountancy.api.requests.UserRequest;
import master.ao.accountancy.api.responses.UserResponse;
import master.ao.accountancy.domain.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper mapper;

    public User toUser(UserRequest request) {
        return mapper.map(request, User.class);
    }

    public UserResponse toUserResponse(User user) {
        return mapper.map(user, UserResponse.class);
    }


}
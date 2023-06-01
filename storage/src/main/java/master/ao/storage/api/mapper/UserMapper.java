package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.UserRequest;
import master.ao.storage.api.response.UserResponse;
import master.ao.storage.core.domain.models.User;
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
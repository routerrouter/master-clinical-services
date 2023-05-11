package master.ao.authuser.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.authuser.api.request.UserRequest;
import master.ao.authuser.api.response.UserResponse;
import master.ao.authuser.core.domain.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper mapper;

    public User toUser(UserRequest request){
        return mapper.map(request, User.class);
    }

    public UserResponse toUserResponse(User User) {
        return mapper.map(User, UserResponse.class);
    }

    public List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
}
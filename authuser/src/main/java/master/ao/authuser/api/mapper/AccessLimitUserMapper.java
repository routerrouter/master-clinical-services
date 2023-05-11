package master.ao.authuser.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.authuser.api.request.AccessLimitRequest;
import master.ao.authuser.api.response.AccessLimitResponse;
import master.ao.authuser.core.domain.model.AcessLimitUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccessLimitUserMapper {

    private final ModelMapper mapper;

    public AcessLimitUser toAcessLimitUser(AccessLimitRequest request){
        return mapper.map(request, AcessLimitUser.class);
    }

    public AccessLimitResponse toAccessLimitResponse(AcessLimitUser acessLimitUser) {
        return mapper.map(acessLimitUser, AccessLimitResponse.class);
    }

    public List<AccessLimitResponse> toAccessLimitResponseList(List<AcessLimitUser> acessLimitUsers) {
        return acessLimitUsers.stream()
                .map(this::toAccessLimitResponse)
                .collect(Collectors.toList());
    }
}
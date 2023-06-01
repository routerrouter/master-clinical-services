package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.GroupRequest;
import master.ao.storage.api.response.GroupResponse;
import master.ao.storage.core.domain.models.Group;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupMapper {

    private final ModelMapper mapper;

    public Group toGroup(GroupRequest request) {
        return mapper.map(request, Group.class);
    }

    public GroupResponse toGroupResponse(Group group) {
        return mapper.map(group, GroupResponse.class);
    }

    public List<GroupResponse> toGroupResponseList(List<Group> groups) {
        return groups.stream()
                .map(this::toGroupResponse)
                .collect(Collectors.toList());
    }
}
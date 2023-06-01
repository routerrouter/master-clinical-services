package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.EntityRequest;
import master.ao.storage.api.response.EntityResponse;
import master.ao.storage.core.domain.models.Entities;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityMapper {

    private final ModelMapper mapper;

    public Entities toEntity(EntityRequest request) {
        return mapper.map(request, Entities.class);
    }

    public EntityResponse toEntityResponse(Entities entity) {
        return mapper.map(entity, EntityResponse.class);
    }


}
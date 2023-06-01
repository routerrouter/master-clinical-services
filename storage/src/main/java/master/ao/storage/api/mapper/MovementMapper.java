package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.MovementRequest;
import master.ao.storage.api.response.MovementResponse;
import master.ao.storage.core.domain.models.Movement;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovementMapper {

    private final ModelMapper mapper;

    public Movement toMovement(MovementRequest request) {
        return mapper.map(request, Movement.class);
    }

    public MovementResponse toMovementResponse(Movement movement) {
        return mapper.map(movement, MovementResponse.class);
    }

    public List<MovementResponse> toMovementResponseList(List<Movement> movements) {
        return movements.stream()
                .map(this::toMovementResponse)
                .collect(Collectors.toList());
    }
}
package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.ItemsMovementRequest;
import master.ao.storage.api.response.ItemsMovementResponse;
import master.ao.storage.core.domain.models.ItemsMovement;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemsItemsMovementMapper {

    private final ModelMapper mapper;

    public ItemsMovement toItemsMovement(ItemsMovementRequest request) {
        return mapper.map(request, ItemsMovement.class);
    }

    public ItemsMovementResponse toItemsMovementResponse(ItemsMovement movement) {
        return mapper.map(movement, ItemsMovementResponse.class);
    }

    public List<ItemsMovementResponse> toItemsMovementResponseList(List<ItemsMovement> itemsMovements) {
        return itemsMovements.stream()
                .map(this::toItemsMovementResponse)
                .collect(Collectors.toList());
    }
}
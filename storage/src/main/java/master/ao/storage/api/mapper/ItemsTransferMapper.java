package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.ItemsTransferRequest;
import master.ao.storage.api.response.ItemsTransferResponse;
import master.ao.storage.core.domain.models.ItemsTransfer;
import master.ao.storage.core.domain.models.Location;
import master.ao.storage.core.domain.models.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemsTransferMapper {

    private final ModelMapper mapper;

    public ItemsTransfer toItemsItemsTransfer(ItemsTransferRequest request) {
        ItemsTransfer item = new ItemsTransfer();
        Product product = new Product();
        product.setProductId(request.getProductId());
        item.setProduct(product);
        Location location = new Location();
        location.setLocationId(request.getLocationId());
        item.setLocation(location);
        return item;
    }

    public ItemsTransferResponse toItemsTransferResponse(ItemsTransfer transfer) {
        return mapper.map(transfer, ItemsTransferResponse.class);
    }

    public List<ItemsTransferResponse> toItemsTransferResponseList(List<ItemsTransfer> transfers) {
        return transfers.stream()
                .map(this::toItemsTransferResponse)
                .collect(Collectors.toList());
    }
}
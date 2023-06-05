package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.StockRequest;
import master.ao.storage.api.response.StockResponse;
import master.ao.storage.core.domain.models.Stock;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StockMapper {

    private final ModelMapper mapper;

    public Stock toStock(StockRequest request) {
        return mapper.map(request, Stock.class);
    }

    public StockResponse toStockResponse(Stock stock) {
        return mapper.map(stock, StockResponse.class);
    }

    public List<StockResponse> toStockResponseList(List<Stock> storages) {
        return storages.stream()
                .map(this::toStockResponse)
                .collect(Collectors.toList());
    }
}
package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.ProductRequest;
import master.ao.storage.api.response.ProductResponse;
import master.ao.storage.core.domain.models.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper mapper;

    public Product toProduct(ProductRequest request) {
        return mapper.map(request, Product.class);
    }

    public ProductResponse toProductResponse(Product product) {
        return mapper.map(product, ProductResponse.class);
    }

    public List<ProductResponse> toProductResponseList(List<Product> products) {
        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }
}
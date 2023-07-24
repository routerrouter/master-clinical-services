package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    Product createProduct(Product product);
    Product updateProduct(Product product, UUID productId);
    void delete(UUID productId);
    Optional<Product> fetchOrFail(UUID productId);
    List<Product> findAll(Specification<Product> spec);
}

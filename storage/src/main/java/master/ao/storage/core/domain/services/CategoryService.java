package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.Category;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Category save(Category category, UUID userId);

    Category update(Category category, UUID categoryId);

    void delete(UUID categoryId);

    Optional<Category> fetchOrFail(UUID categoryId);

    List<Category> findAll(Specification<Category> spec);
}

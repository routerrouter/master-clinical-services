package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.Category;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Category saveCategory(Category category);
    Category updateCategory(Category category, UUID categoryId);
    Optional<Category> fetchOrFail(UUID accountClassId);
    List<Category> findAllCategory(Specification<Category> specification);
    void delete(UUID categoryId);
}

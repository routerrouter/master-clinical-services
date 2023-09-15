package master.ao.accountancy.domain.services.implementation;

import lombok.RequiredArgsConstructor;
import master.ao.accountancy.domain.exceptions.CategoryNotFoundException;
import master.ao.accountancy.domain.exceptions.EntityInUseException;
import master.ao.accountancy.domain.exceptions.ExistingDataException;
import master.ao.accountancy.domain.models.Category;
import master.ao.accountancy.domain.repositories.CategoryRepository;
import master.ao.accountancy.domain.services.CategoryService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String MSG_CATEGORY_IN_USE = "Categoria não pode ser removida, pois está em uso!";

    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category) {
        validateCategory(category);
        category.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        category.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, UUID categoryId) {
        var categoryOptional = fetchOrFail(categoryId);
        categoryOptional.get().setDescription(category.getDescription());

        return categoryRepository.save(categoryOptional.get());
    }

    @Override
    public Optional<Category> fetchOrFail(UUID categoryId) {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        return Optional.of(category);
    }

    @Override
    public List<Category> findAllCategory(Specification<Category> specification) {
        return categoryRepository.findAll(specification);
    }

    @Override
    public void delete(UUID categoryId) {
        try {
            var categoryOptional = fetchOrFail(categoryId).get();
            categoryRepository.delete(categoryOptional);
            categoryRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new CategoryNotFoundException(categoryId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_CATEGORY_IN_USE);
        }
    }

    private void validateCategory(Category category) {
        var categoryDescriptionOptional = categoryRepository.findByDescription(category.getDescription());
        if (categoryDescriptionOptional.isPresent())
            throw new ExistingDataException("Já existe uma categoria registada com esta descrição");
    }
}

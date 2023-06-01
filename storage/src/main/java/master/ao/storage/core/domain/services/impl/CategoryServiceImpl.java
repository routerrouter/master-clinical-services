package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.exceptions.*;
import master.ao.storage.core.domain.models.Category;
import master.ao.storage.core.domain.repositories.CategoryRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.CategoryService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final String MSG_CATEGORY_IN_USE
            = "Categoria não pode ser removido, pois está em uso";


    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Category save(Category category, UUID userId) {
        var categoryOptional = categoryRepository.findByName(category.getName());
        if (categoryOptional.isPresent()) {
            throw new ExistingDataException("Categoria informado já existe.");
        }
        var userGroup = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        category.setUserGroup(userGroup.getGroupId());

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category, UUID categoryId) {
        var categoryOptional = fetchOrFail(categoryId).get();
        categoryOptional.setName(category.getName());
        categoryOptional.setLastUpdateAt(LocalDateTime.now(ZoneId.of("UTC")));
        return categoryRepository.save(categoryOptional);
    }

    @Override
    @Transactional
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

    @Override
    public Optional<Category> fetchOrFail(UUID categoryId) {
        var Category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        return Optional.of(Category);
    }


    @Override
    public List<Category> findAll(Specification<Category> spec) {
        return categoryRepository.findAll(spec);
    }

}
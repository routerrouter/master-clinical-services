package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.exceptions.EntityInUseException;
import master.ao.storage.core.domain.exceptions.ExistingDataException;
import master.ao.storage.core.domain.exceptions.ProductNotFoundException;
import master.ao.storage.core.domain.exceptions.UserNotFoundException;
import master.ao.storage.core.domain.models.Product;
import master.ao.storage.core.domain.repositories.ProductRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.CategoryService;
import master.ao.storage.core.domain.services.GroupService;
import master.ao.storage.core.domain.services.NatureService;
import master.ao.storage.core.domain.services.ProductService;
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
public class ProductServiceImpl implements ProductService {

    private static final String MSG_PRODUCT_IN_USE = "Produto não pode ser removido, pois já foi movimentado.";

    private final ProductRepository repository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final GroupService groupService;
    private final NatureService natureService;

    @Override
    public Product createProduct(Product product, UUID userId) {
        var category = categoryService.fetchOrFail(product.getCategory().getCategoryId());
        var group = groupService.fetchOrFail(product.getGroup().getGroupId());
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));

        if (product.getNatureId() != null) {
            natureService.fetchOrFail(product.getNatureId());
        }

        var productOptional = repository.findByName(product.getName());
        if (productOptional.isPresent()) {
            throw new ExistingDataException("Nome de produto informado já existe!");
        }
        product.setUserGroup(user.getGroupId());
        product.setGroup(group.get());
        product.setCategory(category.get());
        product.setRegisteredAt(LocalDateTime.now(ZoneId.of("UTC")));
        return repository.save(product);
    }

    @Override
    public Product updateProduct(Product product, UUID productId) {
        var productOptional = fetchOrFail(productId).get();
        productOptional.setLastUpdateAt(LocalDateTime.now(ZoneId.of("UTC")));
        productOptional.setName(product.getName());
        productOptional.setMinimumAmount(product.getMinimumAmount());
        productOptional.setCriticalAmount(product.getCriticalAmount());
        productOptional.setBrand(product.getBrand());
        productOptional.setGroup(product.getGroup());
        productOptional.setCategory(product.getCategory());
        productOptional.setNatureId(product.getNatureId());
        return repository.save(productOptional);
    }


    @Override
    @Transactional
    public void delete(UUID productId) {
        try {
            var natureOptional = fetchOrFail(productId).get();
            repository.delete(natureOptional);
            repository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException(productId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_PRODUCT_IN_USE);
        }
    }


    @Override
    public Optional<Product> fetchOrFail(UUID productId) {
        var productOptional = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return Optional.of(productOptional);
    }

    @Override
    public List<Product> findAll(Specification<Product> spec) {
        return repository.findAll(spec);
    }
}







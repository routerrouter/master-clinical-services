package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.Category;
import master.ao.storage.core.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByName(String name);
    Optional<Category> findById(UUID categoryId);

}
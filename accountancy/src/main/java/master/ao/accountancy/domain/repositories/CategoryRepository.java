package master.ao.accountancy.domain.repositories;


import master.ao.accountancy.domain.models.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>  , JpaSpecificationExecutor<Category> {
    Optional<Category> findByDescription(String description);
}

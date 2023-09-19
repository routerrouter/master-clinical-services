package master.ao.accountancy.domain.repositories;


import master.ao.accountancy.domain.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID>  , JpaSpecificationExecutor<Document> {
    Optional<Document> findByDescription(String description);
    Optional<Document> findBySigla(String sigla);
}

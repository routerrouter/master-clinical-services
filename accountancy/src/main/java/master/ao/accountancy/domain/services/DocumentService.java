package master.ao.accountancy.domain.services;

import master.ao.accountancy.domain.models.Document;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentService {
    Document createDocument(Document document);
    Document updateDocument(UUID documentId, Document document);
    Optional<Document> fetchOrFail(UUID documentId);
    List<Document> findAllDocument(Specification<Document> specification);
    void delete(UUID documentId);
    void validateDocument(Document document);
}

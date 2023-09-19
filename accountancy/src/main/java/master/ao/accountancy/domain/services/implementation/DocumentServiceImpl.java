package master.ao.accountancy.domain.services.implementation;

import lombok.AllArgsConstructor;
import master.ao.accountancy.domain.exceptions.DocumentNotFoundException;
import master.ao.accountancy.domain.exceptions.EntityInUseException;
import master.ao.accountancy.domain.exceptions.ExistingDataException;
import master.ao.accountancy.domain.models.Document;
import master.ao.accountancy.domain.repositories.DocumentRepository;
import master.ao.accountancy.domain.services.DocumentService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private static final String MSG_DOCUMENT_IN_USE
            = "Documento não pode ser removido, pois está em uso!";

    private final DocumentRepository documentRepository;

    @Override
    public Document createDocument(Document document) {

        document.setSigla();
        validateDocument(document);

        document.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        document.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return documentRepository.save(document);
    }

    @Override
    public Document updateDocument(UUID documentId, Document document) {
        var documentOptional = fetchOrFail(documentId);
        documentOptional.get().setDescription(document.getDescription());
        document.setSigla();
        documentOptional.get().setSigla(document.getSigla());
        documentOptional.get().setMovementType(document.getMovementType());
        documentOptional.get().setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return documentRepository.save(documentOptional.get());
    }

    @Override
    public Optional<Document> fetchOrFail(UUID documentId) {
        var document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        return Optional.of(document);
    }

    @Override
    public List<Document> findAllDocument(Specification<Document> specification) {
        return documentRepository.findAll(specification);
    }

    @Override
    public void delete(UUID documentId) {
        try {
            var classOptional = fetchOrFail(documentId).get();
            documentRepository.delete(classOptional);
            documentRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new DocumentNotFoundException(documentId);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(MSG_DOCUMENT_IN_USE);
        }
    }

    @Override
    public void validateDocument(Document document) {
        var accountDescriptionOptional = documentRepository.findByDescription(document.getDescription());
        if (accountDescriptionOptional.isPresent())
            throw new ExistingDataException("Já existe um documento registado com esta descrição");

        var accountNumberOptional = documentRepository.findBySigla(document.getSigla());
        if (accountNumberOptional.isPresent())
            throw new ExistingDataException("Já existe um documento registado com esta sigla");
    }
}

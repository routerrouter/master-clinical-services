package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.DocumentRequest;
import master.ao.accountancy.api.responses.DocumentResponse;
import master.ao.accountancy.domain.models.Document;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DocumentMapper {

    private final ModelMapper mapper;

    public Document toDocument(DocumentRequest request) {
        return mapper.map(request, Document.class);
    }

    public DocumentResponse toDocumentResponse(Document document) {
        return mapper.map(document, DocumentResponse.class);
    }

    public List<DocumentResponse> toDocumentResponseList(List<Document> natureList) {
        return natureList.stream()
                .map(this::toDocumentResponse)
                .collect(Collectors.toList());
    }
}

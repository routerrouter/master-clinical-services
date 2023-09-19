package master.ao.accountancy.api.responses;

import lombok.Data;

import java.util.UUID;

@Data
public class DocumentResponse {
    private UUID documentId;
    private String sigla;
    private String description;
    private String movementType;
}

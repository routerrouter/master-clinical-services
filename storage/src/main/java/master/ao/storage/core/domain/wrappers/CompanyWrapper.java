package master.ao.storage.core.domain.wrappers;

import lombok.Data;

import java.util.UUID;

@Data
public class CompanyWrapper {
    private UUID companyId;
    private String contentType;
    private String base64;
}

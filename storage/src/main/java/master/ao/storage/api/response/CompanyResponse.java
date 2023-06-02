package master.ao.storage.api.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CompanyResponse {
    private UUID companyId;
    private String name;
    private String phoneNumber;
    private String address;
    private String nif;
    private String responsible;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
}

package master.ao.storage.api.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

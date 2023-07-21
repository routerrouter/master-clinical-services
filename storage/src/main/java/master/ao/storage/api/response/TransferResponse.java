package master.ao.storage.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import master.ao.storage.core.domain.enums.TransferType;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponse {

    private UUID userId;
    private String description;
    private LocalDate transferDate;
    private TransferType type;
    private StorageResponse storage;

}

package master.ao.storage.api.response;

import lombok.Getter;
import lombok.Setter;
import master.ao.storage.core.domain.models.ItemsTransfer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter

public class TransferResponse {

    private UUID userId;
    private String description;
    private LocalDate transferDate;
    private List<ItemsTransfer> items = new ArrayList<>();
    private UUID userGroup;

}

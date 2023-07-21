package master.ao.storage.api.request;

import lombok.*;
import master.ao.storage.core.domain.audit.Auditable;
import master.ao.storage.core.domain.enums.TransferType;
import master.ao.storage.core.domain.models.ItemsTransfer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    @NotBlank(message = "Descrição da transferencia é obrigatório")
    private String description = "Transferencia de produtos";

    @NotNull(message = "Data da transferencia é obrigatório")
    private LocalDate transferDate;

    @NotNull
    private UUID storageId;

    @NotNull
    private List<ItemsTransferRequest> items  = new ArrayList<>();


}

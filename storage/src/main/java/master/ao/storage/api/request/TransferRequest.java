package master.ao.storage.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

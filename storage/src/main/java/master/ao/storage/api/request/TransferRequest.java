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
import java.util.UUID;

@Setter
@Getter
public class TransferRequest {

    @NotBlank
    private String description;

    @NotNull
    private LocalDate transferDate;

    @NotNull
    private List<ItemsTransferRequest> items = new ArrayList<>();


}

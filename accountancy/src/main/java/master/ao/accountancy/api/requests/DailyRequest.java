package master.ao.accountancy.api.requests;

import lombok.Data;
import master.ao.accountancy.domain.enums.DailyType;
import master.ao.accountancy.domain.models.Document;
import master.ao.accountancy.domain.models.SubAccount;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
public class DailyRequest {


    @NotNull
    private LocalDate dailyDate;

    private boolean status;

    @NotNull
    private UUID natureId;

    @NotBlank
    private String description;

    private String invoiceNumber;

    private String documentUrl;

    @NotNull
    private SubAccount subAccount;

    @NotNull
    private DailyType dailyType;


    @NotNull
    private Document document;

    private String documentNumber;

    @NotNull
    @Min(1)
    private BigDecimal value;
}

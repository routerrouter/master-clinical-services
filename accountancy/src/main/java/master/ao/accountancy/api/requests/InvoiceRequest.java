package master.ao.accountancy.api.requests;

import lombok.Data;
import master.ao.accountancy.domain.models.SubAccount;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class InvoiceRequest {

    @NotNull
    private LocalDate invoiceDate;

    @NotNull
    private SubAccount subAccount;

    @NotNull
    private BigDecimal value;

    private BigDecimal paid;

    private BigDecimal balance;

    private String invoiceUrl;

    @NotNull
    private String invoiceNumber;

    private int situation;

    @NotNull
    private UUID natureId;

    private BigDecimal debt;

    private BigDecimal taxa;
}

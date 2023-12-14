package master.ao.accountancy.api.responses;

import lombok.Data;
import master.ao.accountancy.domain.models.AccountNature;
import master.ao.accountancy.domain.models.SubAccount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class InvoiceResponse {

    private UUID invoiceId;

    private LocalDate invoiceDate;

    private SubAccountDescriptionResponse subAccount;

    private BigDecimal value;

    private BigDecimal paid;

    private BigDecimal balance;

    private String invoiceUrl;

    private String invoiceNumber;

    private int situation;

    private NatureDescriptionResponse nature;

    private BigDecimal debt;

    private BigDecimal taxa;
}

package master.ao.accountancy.api.responses;

import lombok.Data;
import master.ao.accountancy.domain.enums.DailyType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
public class DailyResponse {

    private UUID dailyId;

    private LocalDate dailyDate;

    private LocalDateTime creationDate;

    private boolean status;

    private NatureDescriptionResponse nature;

    private String description;

    private String invoiceNumber;

    private String documentUrl;

    private SubAccountDescriptionResponse subAccount;

    private DailyType dailyType;

    private UUID documentId;

    private String documentNumber;

    private BigDecimal value;
}

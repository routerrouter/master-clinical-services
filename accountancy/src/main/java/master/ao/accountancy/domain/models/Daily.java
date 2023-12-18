package master.ao.accountancy.domain.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import master.ao.accountancy.domain.enums.DailyType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "TB_DAILYS")
public class Daily implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID dailyId;

    private LocalDate dailyDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    private UUID userId;

    private boolean status;

    @ManyToOne()
    @JoinColumn(name = "natureId")
    private AccountNature nature;

    private String description;

    private String invoiceNumber;

    private String documentUrl;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private SubAccount subAccount;

    @Enumerated(EnumType.STRING)
    private DailyType dailyType;

    @ManyToOne
    @JoinColumn(name = "documentId")
    private Document document;

    private String documentNumber;

    private BigDecimal value;
}

package master.ao.accountancy.api.requests;

import master.ao.accountancy.domain.models.Document;

public class DailyRequest {
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
    private MovementType movementType;

    @ManyToOne
    @JoinColumn(name = "documentId")
    private Document document;

    private String documentNumber;

    private BigDecimal value;
}

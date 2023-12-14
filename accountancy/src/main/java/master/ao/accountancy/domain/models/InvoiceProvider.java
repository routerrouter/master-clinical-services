package master.ao.accountancy.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "TB_INVOICES")
public class InvoiceProvider implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID invoiceId;

    private LocalDate invoiceDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    @OneToOne
    @JoinColumn(name = "subAccountId")
    private SubAccount subAccount;

    private UUID userId;

    private BigDecimal value; // valor da fatura

    private BigDecimal paid; // valor pago

    private BigDecimal balance; // saldo da fatura

    private String invoiceUrl;

    private String invoiceNumber;

    private int situation; // 1-Paid, 2-Non paid, 3-parcial

    @OneToOne
    @JoinColumn(name = "dailyId")
    private Daily daily;

    @ManyToOne()
    @JoinColumn(name = "natureId")
    private AccountNature nature;





}

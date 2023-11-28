package master.ao.accountancy.domain.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "TB_PROGRAMMING")
public class FinancialProgramming implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID programmingId;

    @Column(name = "month")
    private String programmingMonth;

    @Column(name = "year")
    private int programmingYear;

    @ManyToOne()
    @JoinColumn(name = "natureId")
    private AccountNature nature;

    @OneToOne
    @JoinColumn(name = "subAccountId")
    private SubAccount subAccount;

    private String document;

    private String description;

    private BigDecimal value;

    private UUID userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    private String ncb;

    private String liq;

    private String os;

    private String finished;

    private String nif;
}

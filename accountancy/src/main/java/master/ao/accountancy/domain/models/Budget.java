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
@Table(name = "TB_BUDGETS")
public class Budget implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID budgetId;

    @Column(name = "year")
    private int currentYear;

    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "natureId")
    private AccountNature nature;

    @Column(nullable = false)
    private BigDecimal value;

    private BigDecimal available;

    private BigDecimal executable;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;
}

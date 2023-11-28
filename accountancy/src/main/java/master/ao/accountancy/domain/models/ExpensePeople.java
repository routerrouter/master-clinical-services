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
@Table(name = "TB_EXPENSE_PEOPLE")
public class ExpensePeople implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID expenseId;

    private Integer year;

    private String month;

    @ManyToOne
    @JoinColumn(name = "natureId")
    private AccountNature nature;

    private UUID userId;

    private BigDecimal value;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;
}

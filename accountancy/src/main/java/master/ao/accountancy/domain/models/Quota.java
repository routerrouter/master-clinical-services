package master.ao.accountancy.domain.models;

// Quota mensal

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
@Table(name = "TB_QUOTA")
public class Quota implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID quotaId;

    @ManyToOne()
    @JoinColumn(name = "natureId")
    private AccountNature nature;

    @Column(name = "month")
    private String quotaMonth;

    @Column(name = "year")
    private int quotaYear;

    private UUID userId;

    private BigDecimal value;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

}

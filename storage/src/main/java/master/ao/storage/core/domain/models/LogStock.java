package master.ao.storage.core.domain.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import master.ao.storage.core.domain.enums.UpdateStockType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "TB_LOG_STOCK")
public class LogStock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID logId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate movementDate;

    @Enumerated(EnumType.STRING)
    private UpdateStockType type;

    private String beforeValue;

    private String alteredValue;

    private String newValue;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    private String lote;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;



}

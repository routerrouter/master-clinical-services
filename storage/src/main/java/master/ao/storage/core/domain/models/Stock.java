package master.ao.storage.core.domain.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import master.ao.storage.core.domain.audit.Auditable;
import master.ao.storage.core.domain.enums.UnitType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_STOCKS")
public class Stock extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID Id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Storage storage;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Location location;

    private String lote;

    private String model;

    private String barcode;

    private String serialNumber;

    @Column(nullable = false)
    private BigDecimal cust;

    private Integer lifespan;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate acquisitionDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate manufactureDate;






}

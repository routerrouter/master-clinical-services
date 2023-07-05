package master.ao.storage.core.domain.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import master.ao.storage.core.domain.audit.Auditable;
import master.ao.storage.core.domain.enums.TransferType;
import master.ao.storage.core.domain.enums.UnitType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_ITEMS_TRANSFER")
public class ItemsTransfer extends Auditable<String> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID itemTransferId;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
    private Transfer transfer;

    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;

    @Column(nullable = false)
    private Long quantity;

    private String lote;

    private Long lifespan;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate acquisitionDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate manufactureDate;

    private String model;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UnitType unitType;




}

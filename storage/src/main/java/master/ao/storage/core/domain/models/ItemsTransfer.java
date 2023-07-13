package master.ao.storage.core.domain.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import master.ao.storage.core.domain.audit.Auditable;
import master.ao.storage.core.domain.enums.UnitType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "TB_ITEMS_TRANSFER")
public class ItemsTransfer extends Auditable<String> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID itemTransferId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_product_id")
    private Product product;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "transfer_id")
    private Transfer transfer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_location_id")
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

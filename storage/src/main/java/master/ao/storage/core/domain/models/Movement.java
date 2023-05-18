package master.ao.storage.core.domain.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import master.ao.storage.core.domain.enums.DevolutionType;
import master.ao.storage.core.domain.enums.MovementStatus;
import master.ao.storage.core.domain.enums.MovementType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="MOVEMENTS")
public class Movement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID movementId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate movementDate;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    private String documentNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Entities entity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementStatus movementStatus;

    @Enumerated(EnumType.STRING)
    private DevolutionType devolutionType;

    @Column(nullable = false)
    private BigDecimal total;

    @Size(min = 15, max = 255)
    private String description;

    @OneToMany(mappedBy = "movement", cascade = CascadeType.ALL)
    private List<ItemsMovement> items = new ArrayList<>();

    public void calculateTotalValue() {
        getItems().forEach(ItemsMovement::calculateTotalValue);

        this.total = getItems().stream()
                .map(item -> item.getTotalValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }


}

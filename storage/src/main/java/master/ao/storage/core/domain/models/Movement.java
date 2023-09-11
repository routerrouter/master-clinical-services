package master.ao.storage.core.domain.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity
@Table(name = "TB_MOVEMENTS")
public class Movement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID movementId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate movementDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    private String documentNumber;

    private String fileName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "entityId")
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

    private String patient;

    private UUID userGroup;

    @JsonIgnore
    @OneToMany(mappedBy = "movement", cascade = CascadeType.ALL)
    private List<ItemsMovement> items = new ArrayList<>();

    public void calculateTotalValue() {
        getItems().forEach(ItemsMovement::calculateTotalValue);

        this.total = getItems()
                .stream()
                .map(item -> item.getTotalValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (this.total.doubleValue() <0 ) {
            double newTotal = this.total.doubleValue()*-1;
            this.total = BigDecimal.valueOf(newTotal);
        }

    }

    public void setMovementStatus() {
        if (this.getMovementType().equals(MovementType.REQUEST) || this.getMovementType().equals(MovementType.ORDER)) {
            this.movementStatus = MovementStatus.PENDING;
        } else {
            this.movementStatus = MovementStatus.FINISHED;
        }
    }


}

package master.ao.storage.core.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import master.ao.storage.core.domain.enums.EntityType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Setter
@Getter
@Entity
@Table(name = "TB_ENTITIES")
public class Entities implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID entityId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    private String name;

    private String address;

    private String phoneNumber;

    private String email;

    @Column(nullable = false)
    private String nif;

    @Column(nullable = false)
    private String responsible;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private UUID userGroup;

    @JsonIgnore
    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL)
    private List<Movement> movements = new ArrayList<>();


}

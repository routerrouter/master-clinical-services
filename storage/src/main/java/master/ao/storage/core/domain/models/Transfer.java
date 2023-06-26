package master.ao.storage.core.domain.models;

import lombok.*;
import master.ao.storage.core.domain.audit.Auditable;
import master.ao.storage.core.domain.enums.TransferType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_TRANSFERS")
public class Transfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transferId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate transferDate;

    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL)
    private List<ItemsTransfer> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TransferType type;

    private UUID userGroup;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;





}

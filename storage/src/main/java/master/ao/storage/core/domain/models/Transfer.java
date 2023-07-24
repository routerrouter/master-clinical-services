package master.ao.storage.core.domain.models;

import lombok.Getter;
import lombok.Setter;
import master.ao.storage.core.domain.enums.TransferType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
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
    private Set<ItemsTransfer> items;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;





}

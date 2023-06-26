package master.ao.storage.core.domain.models;

import lombok.Data;
import master.ao.storage.core.domain.audit.Auditable;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_COMPANY_IMAGE")
public class CompanyImage extends Auditable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Company company;

    private String contentType;

    @Lob
    private String base64;
}

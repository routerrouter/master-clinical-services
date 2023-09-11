package master.ao.authuser.core.domain.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_STORAGES")
public class Storage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID storageId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID userGroup;

}

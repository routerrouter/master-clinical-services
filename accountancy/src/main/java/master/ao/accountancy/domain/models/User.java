package master.ao.accountancy.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "TB_USERS")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    private UUID groupId;


}

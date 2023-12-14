package master.ao.accountancy.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "TB_SUBACCOUNT_NATURES")
public class ProviderNature implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "subAccountId")
    private SubAccount subAccount;

    @ManyToOne
    @JoinColumn(name = "natureId")
    private AccountNature nature;
}

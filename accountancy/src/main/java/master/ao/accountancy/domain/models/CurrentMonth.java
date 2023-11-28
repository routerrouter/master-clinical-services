package master.ao.accountancy.domain.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "TB_CURRENT_YEAR")
public class CurrentMonth implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID currentYearId;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private String month;

}

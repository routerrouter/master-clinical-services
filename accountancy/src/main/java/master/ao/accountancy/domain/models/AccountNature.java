package master.ao.accountancy.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "TB_NATURES")
public class AccountNature implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID natureId;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;
}

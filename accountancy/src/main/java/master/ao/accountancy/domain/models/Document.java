package master.ao.accountancy.domain.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import master.ao.accountancy.domain.enums.MovementType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "TB_DOCUMENTS")
public class Document implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID documentId;

    @Column(nullable = false)
    private String sigla;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;


    public void setSigla() {
        String sigla = "";
        boolean characterAnteriorEraEspaco = true;

        for (int i = 0; i < this.description.length(); i++) {

            char character = this.description.charAt(i);
            if (Character.isUpperCase(character) && characterAnteriorEraEspaco) {
                System.out.print(character);
                sigla = sigla.concat(String.valueOf(character));
            }

            characterAnteriorEraEspaco = Character.isWhitespace(character);
        }
        this.sigla = sigla;
    }
}

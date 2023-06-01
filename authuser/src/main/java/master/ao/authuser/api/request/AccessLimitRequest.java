package master.ao.authuser.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AccessLimitRequest {

    private LocalDate ativation;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate blockDate;

    @Column(nullable = false)
    private Long days;

    @Column(nullable = false)
    private UUID userId;
}

package master.ao.authuser.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import master.ao.authuser.core.domain.model.User;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AccessLimitResponse {

    private UUID accessLimitId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate ativation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate blockDate;

    private Long days;
    private boolean status;

}

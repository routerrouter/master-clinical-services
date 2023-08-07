package master.ao.accountancy.api.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountClassResponse {

    private UUID accountClassId;
    private String number;
    private String description;
}

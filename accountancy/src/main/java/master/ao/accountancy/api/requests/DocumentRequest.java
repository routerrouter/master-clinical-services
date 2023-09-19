package master.ao.accountancy.api.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class DocumentRequest {


    @NotBlank
    private String description;

    private String movementType;

}

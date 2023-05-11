package master.ao.authuser.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GroupRequest {

    @NotBlank
    private String description;
}

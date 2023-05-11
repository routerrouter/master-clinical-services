package master.ao.authuser.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RoleRequest {

    @NotBlank
    private String description;
}

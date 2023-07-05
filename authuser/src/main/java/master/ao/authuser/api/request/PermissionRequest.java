package master.ao.authuser.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PermissionRequest {

    @NotBlank
    private String description;
    private String icon;

}

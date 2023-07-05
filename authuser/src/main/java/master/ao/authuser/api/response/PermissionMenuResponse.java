package master.ao.authuser.api.response;

import lombok.Data;

import java.util.UUID;

@Data
public class PermissionMenuResponse {
    private String label;
    private String icon;
    private String to;
}

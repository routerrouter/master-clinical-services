package master.ao.authuser.api.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuItem {
    private String icon;
    private String label;
    private String to;
    private List<Object> subs;
}

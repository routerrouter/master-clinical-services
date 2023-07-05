package master.ao.authuser.api.response;

import java.util.List;

public interface MenuView {

    String getDescription();
    String getRoute();
    String getIcon();

    List<RoleView> getRoles();

    interface RoleView {
        String getDescription();
    }
}

package master.ao.authuser.core.domain.service;

import master.ao.authuser.core.domain.model.User;
import master.ao.authuser.core.domain.model.UserAttempts;

public interface UserAttemptsService {
    void updateFailAttempts(String username);

    void resetFailAttempts(String username);

    void increaseFailedAttempts(UserAttempts userAttempts);

    void lock(User user);

    boolean unlockWhenTimeExpired(User user);

    void onAuthenticationFailure(String username);

    void onAuthenticationSuccess(String username);

    UserAttempts getUserAttempts(String username);
}

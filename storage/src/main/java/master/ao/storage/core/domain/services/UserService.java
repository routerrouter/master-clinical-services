package master.ao.storage.core.domain.services;

import master.ao.storage.core.domain.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void saveUser(User user);
    Optional<User> fetchOrFail(UUID userId);
}

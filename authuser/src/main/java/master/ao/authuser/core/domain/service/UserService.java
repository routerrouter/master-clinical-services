package master.ao.authuser.core.domain.service;

import master.ao.authuser.core.domain.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User saveUser(User user, UUID groupId);
    void deleteUser(UUID userId);
    User updateUser(User user,UUID userId);
    void resetPassword(UUID userId,User user);
    Optional<User> fetchOrFail(UUID userId);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findAll(Specification<User> spec);
    List<User> findAll(UUID userId);
    void removeRoles(UUID userId, List<UUID> rolesId);
    void associateRoles(UUID userId, List<UUID> roles);

}

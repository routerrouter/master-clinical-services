package master.ao.authuser.core.domain.service;

import master.ao.authuser.api.request.UserRequest;
import master.ao.authuser.core.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User saveUser(UserRequest user);
    void deleteUser(UUID userId);
    User updateUser(User user);
    void resetPassword(UUID userId,UserRequest user);
    Optional<User> findById(UUID userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Page<User> findAll(Specification<User> spec, Pageable pageable);

}

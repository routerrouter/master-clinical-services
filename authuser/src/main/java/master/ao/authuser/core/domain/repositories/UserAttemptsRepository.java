package master.ao.authuser.core.domain.repositories;


import master.ao.authuser.core.domain.model.UserAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAttemptsRepository extends JpaRepository<UserAttempts, UUID> {
    Optional<UserAttempts> findByUsername(String username);
}

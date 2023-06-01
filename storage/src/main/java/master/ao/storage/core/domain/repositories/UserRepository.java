package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID userId);

    @Modifying
    @Query(value = "INSERT INTO users (user_id, creation_date, email, full_name, group_id, username)\n" +
            "VALUES(:userId, :creationDate, :email, :fullName, :groupId, :username)", nativeQuery = true)
    void saveUserStorage(UUID userId, LocalDateTime creationDate, String email, String fullName, UUID groupId, String username);


    @Modifying
    @Query(value = "UPDATE  users set email=?3, full_name=?4, username=?5,last_update_date=?2 where user_id=?1", nativeQuery = true)
    void updateUserStorage(UUID userId, LocalDateTime lastUpdateDate, String email, String fullName, String username);

}
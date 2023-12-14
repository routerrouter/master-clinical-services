package master.ao.accountancy.domain.repositories;

import master.ao.accountancy.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID userId);

    @Modifying
    @Query(value = "INSERT INTO tb_users (user_id, email, full_name, group_id, username)\n" +
            "VALUES(:userId, :email, :fullName, :groupId, :username)", nativeQuery = true)
    void saveUserAccountancy(UUID userId, String email, String fullName, UUID groupId, String username);


    @Modifying
    @Query(value = "UPDATE  tb_users set email=?2, full_name=?3, username=?4 where user_id=?1", nativeQuery = true)
    void updateUserAccountancy(UUID userId,String email, String fullName, String username);

}
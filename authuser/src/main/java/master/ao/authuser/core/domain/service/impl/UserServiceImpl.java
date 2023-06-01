package master.ao.authuser.core.domain.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.clients.UserClient;
import master.ao.authuser.api.request.UserStorageRequest;
import master.ao.authuser.core.domain.exception.BadCredentialsException;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.exception.UserNotFoundException;
import master.ao.authuser.core.domain.model.User;
import master.ao.authuser.core.domain.repository.UserRepository;
import master.ao.authuser.core.domain.service.GroupService;
import master.ao.authuser.core.domain.service.UserService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final GroupService groupService;
    private final PasswordEncoder passwordEncoder;
    private final UserClient userClient;


    @Override
    public Optional<User> fetchOrFail(UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Nome de usuário informado não encontrado!"));
        return Optional.of(user);
    }


    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAll(Specification<User> spec) {
        return userRepository.findAll(spec);
    }

    @Override
    public List<User> findAll(UUID userId) {
        return null;
    }

    @Override
    @Transactional
    public void removeRoles(UUID userId, List<UUID> roleIds) {
        var user = fetchOrFail(userId).get();

        roleIds.forEach(roleId -> {
            if (userRepository.existUserRoles(user.getUserId(), roleId) > 0L) {
                userRepository.removeRoles(user.getUserId(), roleId);
            }
        });
    }

    @Override
    @Transactional
    public void associateRoles(UUID userId, List<UUID> roles) {
        var user = fetchOrFail(userId).get();

        roles.forEach(roleId -> {
            if (userRepository.existUserRoles(user.getUserId(), roleId) == 0L) {
                userRepository.associateRoles(user.getUserId(), roleId);
            }
        });
    }


    @Transactional
    @Override
    public User saveUser(User user, UUID groupId, String token) {

        if (existsByUsername(user.getUsername())) {
            log.warn("Username {} is Already Taken ", user.getUsername());
            throw new BussinessException("O usuário informado já existe!");
        }
        if (existsByEmail(user.getEmail())) {
            log.warn("Email {} is Already Taken ", user.getEmail());
            throw new BussinessException("O email informado já existe!");
        }

        var group = groupService.fetchOrFail(groupId);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        user.setLockTime(new Date());
        user.setGroup(group.get());
        user = userRepository.save(user);
        log.debug("POST registerUser userId saved {} ", user.getUserId());
        log.info("User saved successfully userId {} ", user.getUserId());
        saveOrUpdateUserToStorage(user, token);

        return user;
    }


    @Override
    public void deleteUser(UUID userId) {
        var user = fetchOrFail(userId).get();
        user.setEnabled(false);
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(User userRequest, UUID userId, String token) {
        var user = fetchOrFail(userId).get();

        user.setUsername(userRequest.getUsername());
        user.setFullName(userRequest.getFullName());
        user.setEmail(userRequest.getEmail());
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        user = userRepository.save(user);

        saveOrUpdateUserToStorage(user, token);

        return user;
    }

    @Override
    public void resetPassword(UUID userId, User user) {
        var userOptional = fetchOrFail(userId);
        var userUpdated = userOptional.get();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordIsMatched(user.getOldPassword(), userUpdated.getPassword())) {
            userUpdated.setPassword(passwordEncoder.encode(user.getPassword()));
            userUpdated.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userRepository.save(userUpdated);
        } else {
            throw new BussinessException("Senhas não combinam. Certifica-se que está inserindo correctamente a senha antiga");
        }

    }

    public boolean passwordIsMatched(String password, String oldPawword) {
        return passwordEncoder.matches(password, oldPawword);
    }

    private void saveOrUpdateUserToStorage(User user, String token) {
        var userStorage = new UserStorageRequest();
        userStorage.setCreationDate(user.getCreationDate());
        userStorage.setLastUpdateDate(user.getLastUpdateDate());
        userStorage.setEmail(user.getEmail());
        userStorage.setUsername(user.getUsername());
        userStorage.setFullName(user.getFullName());
        userStorage.setGroupId(user.getGroup().getGroupId());
        userStorage.setUserId(user.getUserId());
        userClient.saveUserToStorageAndOuther(userStorage, token);
    }

}
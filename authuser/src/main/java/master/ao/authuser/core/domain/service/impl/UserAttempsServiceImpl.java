package master.ao.authuser.core.domain.service.impl;

import lombok.RequiredArgsConstructor;
import master.ao.authuser.core.domain.exception.AccountLockedException;
import master.ao.authuser.core.domain.model.User;
import master.ao.authuser.core.domain.model.UserAttempts;
import master.ao.authuser.core.domain.repository.UserAttemptsRepository;
import master.ao.authuser.core.domain.repository.UserRepository;
import master.ao.authuser.core.domain.service.UserAttemptsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserAttempsServiceImpl implements UserAttemptsService {

    private final UserAttemptsRepository attemptsRepository;
    private final UserRepository userRepository;
    public static final int MAX_FAILED_ATTEMPTS = 3; // Attempts number
    private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours

    @Override
    public void updateFailAttempts(String username) {

        var userAttemptsOptional = attemptsRepository.findByUsername(username);
        var userAttempts = new UserAttempts();

        if (userAttemptsOptional.isPresent()) {
            userAttempts = userAttemptsOptional.get();
            userAttempts.setFailedAttempt(userAttempts.getFailedAttempt() + 1);
        } else {
            userAttempts.setUsername(username);
            userAttempts.setFailedAttempt(1);
        }
        userAttempts.setLastModified(LocalDateTime.now(ZoneId.of("UTC")));
        attemptsRepository.save(userAttempts);

    }

    @Override
    public void resetFailAttempts(String username) {
        var userAttemptsOptional = attemptsRepository.findByUsername(username);
        var userAttempts = new UserAttempts();
        if (userAttemptsOptional.isPresent()) {
            userAttempts = userAttemptsOptional.get();
            userAttempts.setFailedAttempt(0);
            userAttempts.setLastModified(LocalDateTime.now(ZoneId.of("UTC")));
        } else {
            userAttempts.setFailedAttempt(0);
            userAttempts.setLastModified(LocalDateTime.now(ZoneId.of("UTC")));
            userAttempts.setUsername(username);
        }
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário informado não encontrado."));
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        userRepository.save(user);
        attemptsRepository.save(userAttempts);

    }

    @Override
    public void increaseFailedAttempts(UserAttempts userAttempts) {
        updateFailAttempts(userAttempts.getUsername());
    }

    @Override
    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
        throw new AccountLockedException("Sua conta foi bloqueada devido a 3 tentativas mal sucedidas."
                + " Ele será desbloqueado após 24 horas.");
    }

    @Override
    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION <= currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(new Date());
            resetFailAttempts(user.getUsername());
            userRepository.save(user);

            return true;
        }

        return false;
    }

    @Override
    public void onAuthenticationFailure(String username) {
        var userAttempts = attemptsRepository.findByUsername(username);
        var user = userRepository.findByUsername(username).get();

        if (userAttempts.isPresent()) {
            if (user != null) {
                if (user.isEnabled() && user.isAccountNonLocked()) {
                    if (userAttempts.get().getFailedAttempt() < UserAttempsServiceImpl.MAX_FAILED_ATTEMPTS - 1) {
                        increaseFailedAttempts(userAttempts.get());
                    } else {
                        lock(user);
                    }
                } else if (!user.isAccountNonLocked()) {
                    if (!unlockWhenTimeExpired(user)) {
                        new AccountLockedException("Sua conta foi bloqueada. Por favor tente o Login mais tarde");
                    }
                }

            }
        } else {
            updateFailAttempts(username);
        }

    }

    @Override
    public void onAuthenticationSuccess(String username) {
        var userAttempts = attemptsRepository.findByUsername(username);
        if (userAttempts.isPresent()) {
            var user = userRepository.findByUsername(username).get();
            if (userAttempts.get().getFailedAttempt() > 0) {
                resetFailAttempts(user.getUsername());
            }
        }

    }

    @Override
    public UserAttempts getUserAttempts(String username) {
        return attemptsRepository.findByUsername(username).get();
    }


}

package master.ao.accountancy.domain.services.implementation;

import master.ao.accountancy.domain.exceptions.UserNotFoundException;
import master.ao.accountancy.domain.models.User;
import master.ao.accountancy.domain.repositories.UserRepository;
import master.ao.accountancy.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public void saveUser(User user) {
        var userOptional = userRepository.findById(user.getUserId());
        if (userOptional.isPresent()) {
            userRepository.updateUserAccountancy(user.getUserId(), user.getEmail(), user.getFullName(), user.getUsername());
        } else {
            userRepository.saveUserAccountancy(user.getUserId(), user.getEmail(), user.getFullName(), user.getGroupId(), user.getUsername());
        }

    }

    @Override
    public Optional<User> fetchOrFail(UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return Optional.of(user);
    }

}

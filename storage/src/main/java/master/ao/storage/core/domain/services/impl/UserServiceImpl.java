package master.ao.storage.core.domain.services.impl;

import master.ao.storage.core.domain.models.User;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public void saveUser(User user) {
        var userOptional = userRepository.findById(user.getUserId());
        if (userOptional.isPresent()) {
            userRepository.updateUserStorage(user.getUserId(), user.getLastUpdateDate(), user.getEmail(), user.getFullName(), user.getUsername());
        } else {
            userRepository.saveUserStorage(user.getUserId(), user.getCreationDate(), user.getEmail(), user.getFullName(), user.getGroupId(), user.getUsername());
        }

    }

}

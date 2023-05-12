package master.ao.authuser.core.domain.service.impl;

import master.ao.authuser.core.domain.exception.AccessLimitNotFoundException;
import master.ao.authuser.core.domain.model.AcessLimitUser;
import master.ao.authuser.core.domain.repository.AccessLimitUserRepository;
import master.ao.authuser.core.domain.repository.UserRepository;
import master.ao.authuser.core.domain.service.AccessLimitUserService;
import master.ao.authuser.core.domain.service.UserService;
import master.ao.authuser.core.domain.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccessLimitUserServiceImpl implements AccessLimitUserService {

    private final AccessLimitUserRepository repository;
    private final UserRepository userRepository;
    private final DateUtils dateUtils;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    public AccessLimitUserServiceImpl(AccessLimitUserRepository repository, UserRepository userRepository, DateUtils dateUtils) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.dateUtils = dateUtils;
    }


    @Override
    public AcessLimitUser save(AcessLimitUser acessLimitUser, UUID userId) {

        var userOptional = userService.fetchOrFail(userId);
        acessLimitUser.setUser(userOptional.get());

        return repository.save(acessLimitUser);
    }

    @Override
    public AcessLimitUser update(AcessLimitUser acessLimitUser, UUID userId) {

        var userOptional =  userService.fetchOrFail(userId).get();
        var accessLimitOptional = fetchOrFailByUserId(userId).get();

        long days = dateUtils.getDiferenceBetweenDatesIndDays(acessLimitUser.getAtivation(), acessLimitUser.getBlockDate());
        accessLimitOptional.setAtivation(acessLimitUser.getAtivation());
        accessLimitOptional.setBlockDate(acessLimitUser.getBlockDate());
        if (days == 0) {
            userOptional.setAccountNonLocked(false);
            userOptional.setCredentialsNonExpired(false);
            userOptional.setAccountNonExpired(false);
            userRepository.save(userOptional);
        }
        return repository.save(accessLimitOptional);
    }

    @Override
    public Optional<AcessLimitUser> fetchOrFailByUserId(UUID userId) {

        var userOptional = userService.fetchOrFail(userId).get();
        var acessLimit =  repository.findAcessLimitUserByUser(userOptional.getUserId())
                .orElseThrow( ()-> new AccessLimitNotFoundException(userId));

        return Optional.of(acessLimit);

    }

    @Override
    public List<AcessLimitUser> findAll() {
        return repository.findAll();
    }

}

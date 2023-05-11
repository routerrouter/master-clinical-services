package master.ao.authuser.core.domain.service.impl;

import master.ao.authuser.core.domain.model.AcessLimitUser;
import master.ao.authuser.core.domain.repository.AccessLimitUserRepository;
import master.ao.authuser.core.domain.repository.UserRepository;
import master.ao.authuser.core.domain.service.AccessLimitUserService;
import master.ao.authuser.core.domain.utils.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccessLimitUserServiceImpl implements AccessLimitUserService {

    private final AccessLimitUserRepository repository;
    private final UserRepository userRepository;
    private final DateUtils dateUtils;

    public AccessLimitUserServiceImpl(AccessLimitUserRepository repository, UserRepository userRepository, DateUtils dateUtils) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.dateUtils = dateUtils;
    }


    @Override
    public AcessLimitUser save(AcessLimitUser acessLimitUser, UUID userId) {

        var userOptional = userRepository.findById(userId)
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário informado não encontrado.") );

        long days = dateUtils.getDiferenceBetweenDatesIndDays(acessLimitUser.getAtivation(), acessLimitUser.getBlockDate());
        acessLimitUser.setUser(userOptional);
        return repository.save(acessLimitUser);
    }

    @Override
    public AcessLimitUser update(AcessLimitUser acessLimitUser, UUID userId) {

        var userOptional = userRepository.findById(userId)
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário informado não encontrado.") );

        var accessLimitOptional = repository.findAcessLimitUserByUser(userOptional.getUserId())
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Informação solicitada não encontrado.") );

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
    public Optional<AcessLimitUser> findByUserId(UUID userId) {

        var userOptional = userRepository.findById(userId)
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário informado não encontrado.") );

        return repository.findAcessLimitUserByUser(userOptional.getUserId());
    }

    @Override
    public List<AcessLimitUser> findAll() {
        return repository.findAll();
    }

    public boolean setStatus(long days) {
        return days > 0L ? true : false;
    }
}

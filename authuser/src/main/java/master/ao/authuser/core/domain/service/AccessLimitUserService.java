package master.ao.authuser.core.domain.service;

import master.ao.authuser.core.domain.model.AcessLimitUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccessLimitUserService {
    AcessLimitUser save(AcessLimitUser acessLimitUser, UUID userId);
    AcessLimitUser update(AcessLimitUser acessLimitUser, UUID userId);
    Optional<AcessLimitUser> findByUserId(UUID userId);
    List<AcessLimitUser> findAll();
}

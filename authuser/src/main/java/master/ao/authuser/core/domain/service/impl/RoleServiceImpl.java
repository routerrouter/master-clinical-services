package master.ao.authuser.core.domain.service.impl;


import master.ao.authuser.core.domain.model.Role;
import master.ao.authuser.core.domain.repository.RoleRepository;
import master.ao.authuser.core.domain.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Role save(Role role) {
        return null;
    }
}

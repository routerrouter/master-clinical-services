package master.ao.authuser.core.domain.service.impl;

import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.mapper.UserMapper;
import master.ao.authuser.api.request.UserRequest;
import master.ao.authuser.core.domain.enums.UserStatus;
import master.ao.authuser.core.domain.model.Role;
import master.ao.authuser.core.domain.model.User;
import master.ao.authuser.core.domain.repository.RoleRepository;
import master.ao.authuser.core.domain.repository.UserRepository;
import master.ao.authuser.core.domain.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper mapper;


    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
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
    public Page<User> findAll(Specification<User> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }


    @Transactional
    @Override
    public User saveUser(UserRequest userRequest) {

        log.debug("POST registerUser userDto received {} ", userRequest.toString());
        if(userRepository.existsByUsername(userRequest.getUsername())){
            log.warn("Username {} is Already Taken ", userRequest.getUsername());
            new ResponseStatusException(HttpStatus.CONFLICT,"Error: Username is Already Taken!");
        }
        if(userRepository.existsByEmail(userRequest.getEmail())){
            log.warn("Email {} is Already Taken ", userRequest.getEmail());
            new ResponseStatusException(HttpStatus.CONFLICT,"Error: Email is Already Taken!");
        }

        Role role = roleRepository.findByDescription(userRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: Role is Not Found."));

        var user = mapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserStatus(UserStatus.ACTIVE);
        user.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        user.addRoles(role);
        userRepository.save(user);
        log.debug("POST registerUser userId saved {} ", user.getUserId());
        log.info("User saved successfully userId {} ", user.getUserId());
        //userEventPublisher.publishUserEvent(User.convertToUserEventDto(), ActionType.CREATE);
        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        var User = userRepository.findById(userId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
        User.setUserStatus(UserStatus.BLOCKED);
        //userEventPublisher.publishUserEvent(User.convertToUserEventDto(), ActionType.DELETE);
        userRepository.save(User);
    }

    @Override
    public User updateUser(User userRequest) {
        var User = userRepository.findById(userRequest.getUserId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
        BeanUtils.copyProperties(userRequest,User);
        User.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        User = userRepository.save(User);
        BeanUtils.copyProperties(User, userRequest);
        return userRequest;
    }

    @Override
    public void resetPassword(UUID userId,UserRequest userRequest) {
        var optUser = userRepository.findById(userId);
        if(!optUser.isPresent()){
            new ResponseStatusException(HttpStatus.CONFLICT,"User not found");
        } if(!optUser.get().getPassword().equals(userRequest.getOldPassword())){
            new ResponseStatusException(HttpStatus.CONFLICT,"Error: Mismatched old password!");
        } else {
            var user = optUser.get();
            user.setPassword(userRequest.getPassword());
            user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userRepository.save(user);
        }
    }


    /*
    * private UsuarioDto getUserDto(Usuario usuario) {
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setEmail(usuario.getEmail());
		usuarioDto.setEnabled(usuario.isEnabled());
		usuarioDto.setFirstname(usuario.getFirstname());
		usuarioDto.setCodigo(usuario.getCodigo());
		usuarioDto.setLastname(usuario.getLastname());
		usuarioDto.setUsername(usuario.getUsername());
		usuarioDto.setPassword(usuario.getPassword());

		Set<RoleDto> rSet = new HashSet<>();
		for(Role r : usuario.getRoles()) {
			RoleDto rDto = new RoleDto();
			rDto.setName(r.getName());
			Set<AuthorityDto> aSet = new HashSet<>();

			for(Authority a : r.getAuthorities()) {
				AuthorityDto aDto = new AuthorityDto(a.getName());
				aSet.add(aDto);
			}
			rDto.setAuthorities(aSet);
			rSet.add(rDto);
		}
		usuarioDto.setRoles(rSet);

		return usuarioDto;
	}
    * */



}
package master.ao.authuser.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.config.security.JwtProvider;
import master.ao.authuser.api.request.JwtRequest;
import master.ao.authuser.api.request.LoginRequest;
import master.ao.authuser.api.request.UserRequest;
import master.ao.authuser.core.domain.service.RoleService;
import master.ao.authuser.core.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserRequest.UserView.RegistrationPost.class)
                                                   @JsonView(UserRequest.UserView.RegistrationPost.class) UserRequest requestUser){
        /*log.debug("POST registerUser userDto received {} ", userDto.toString());
        if(userService.existsByUsername(userDto.getUsername())){
            log.warn("Username {} is Already Taken ", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
        }
        if(userService.existsByEmail(userDto.getEmail())){
            log.warn("Email {} is Already Taken ", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
        }
        RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Error: Role is Not Found."));
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.getRoles().add(roleModel);
        userService.saveUser(userModel);
        log.debug("POST registerUser userId saved {} ", userModel.getUserId());
        log.info("User saved successfully userId {} ", userModel.getUserId());*/
        return  null;//ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

   @PostMapping("/login")
    public ResponseEntity<JwtRequest> authenticateUser(@Valid @RequestBody LoginRequest requestLogin) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestLogin.getUsername(), requestLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtRequest(jwt));
    }

}

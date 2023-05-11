package master.ao.authuser.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.config.security.JwtProvider;
import master.ao.authuser.api.mapper.UserMapper;
import master.ao.authuser.api.request.JwtRequest;
import master.ao.authuser.api.request.LoginRequest;
import master.ao.authuser.api.request.UserRequest;
import master.ao.authuser.api.response.UserResponse;
import master.ao.authuser.core.domain.exception.AccountAccessLimitException;
import master.ao.authuser.core.domain.exception.AccountExpiredException;
import master.ao.authuser.core.domain.exception.AccountLockedException;
import master.ao.authuser.core.domain.exception.BadCredentialsException;
import master.ao.authuser.core.domain.service.AccessLimitUserService;
import master.ao.authuser.core.domain.service.UserAttemptsService;
import master.ao.authuser.core.domain.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {


    private final UserService userService;
    private final UserAttemptsService userAttemptsService;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final AccessLimitUserService limitUserService;



    @PostMapping("/signup/{groupId}/group")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @JsonView(UserRequest.UserView.RegistrationPost.class) UserRequest request,
                                                     @PathVariable("groupId") UUID groupId) {
        log.debug("POST createUser request received {} ", request.toString());

        return Stream.of(request)
                .map(mapper::toUser)
                .map(user -> userService.saveUser(user, groupId))
                .map(mapper::toUserResponse)
                .map(userResponse -> ResponseEntity.status(HttpStatus.CREATED).body(userResponse))
                .findFirst()
                .get();

    }

    @PostMapping("/login")
    public ResponseEntity<?> authentication(@Valid @RequestBody LoginRequest requestLogin) throws AuthenticationException {

        var user = userService.findByUsername(requestLogin.getUsername()).get();
        userAttemptsService.unlockWhenTimeExpired(user);
        var userAccess = limitUserService.findByUserId(user.getUserId()).get();

        try {
            if (userAccess.getBlockDate().isAfter(LocalDate.now())) {
                if (user.isAccountNonLocked()) {
                    if (user.isAccountNonExpired()) {
                        Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(requestLogin.getUsername(), requestLogin.getPassword()));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String jwt = jwtProvider.generateJwt(authentication);

                        userAttemptsService.onAuthenticationSuccess(requestLogin.getUsername());

                        return ResponseEntity.ok(new JwtRequest(jwt));
                    } else {
                        throw new AccountExpiredException("Sua conta encontra-se com o acesso expirado. Por favor consultar o Administrador do sistema.");
                    }

                } else {
                    throw new AccountLockedException("Sua conta foi bloqueada devido a 3 tentativas malsucedidas."
                            + " Ele será desbloqueado após 24 horas.");
                }
            }else {
                throw new AccountAccessLimitException("Sua conta atingiu o limite de acesso. Por favor consultar o Administrador do sistema.");
            }


        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            log.error("Problema ao fazer login do usuário com credenciais!", e);
            userAttemptsService.onAuthenticationFailure(requestLogin.getUsername());
            var attempts = userAttemptsService.getUserAttempts(requestLogin.getUsername()).getFailedAttempt();
            throw new BadCredentialsException("Credenciais informadas estão erradas, verifique e tente novamente."
                    + " Restam-lhe apenas " + (3 - attempts) + " tentativas. Consulte o administrador para resset de sua senha.");
        }

    }

}

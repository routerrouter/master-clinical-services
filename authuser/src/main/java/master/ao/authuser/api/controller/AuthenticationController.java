package master.ao.authuser.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.config.security.JwtProvider;
import master.ao.authuser.api.mapper.UserMapper;
import master.ao.authuser.api.request.LoginRequest;
import master.ao.authuser.api.request.LoginResponseDetail;
import master.ao.authuser.api.request.UserRequest;
import master.ao.authuser.api.response.GroupResponse;
import master.ao.authuser.api.response.UserResponse;
import master.ao.authuser.core.domain.exception.*;
import master.ao.authuser.core.domain.model.Storage;
import master.ao.authuser.core.domain.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication|Signup", description = "The Authentication API. Contains all operations that can be performed on a authentication and signup user.")
public class AuthenticationController {


    private final UserService userService;
    private final UserAttemptsService userAttemptsService;
    private final UserMapper mapper;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final AccessLimitUserService limitUserService;
    private final RoleService roleService;
    private final StorageService storageService;

    @Operation(summary = "Create account user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created!",
                    content = @Content(schema = @Schema(implementation = GroupResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping("/signup/{groupId}/group")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody @JsonView(UserRequest.UserView.RegistrationPost.class) UserRequest request,
                                                     @Parameter(description = "id of group to be associate") @PathVariable("groupId") UUID groupId,
                                                     @RequestHeader("Authorization") String token) {
        log.debug("POST createUser request received {} ", request.toString());


        return Stream.of(request)
                .map(mapper::toUser)
                .map(user -> userService.saveUser(user, groupId, token))
                .map(mapper::toUserResponse)
                .map(userResponse -> ResponseEntity.status(HttpStatus.CREATED).body(userResponse))
                .findFirst()
                .get();

    }

    @Operation(summary = "Authentication|Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Logged!", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "No authorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping("/login")
    public ResponseEntity<?> authentication(@Valid @RequestBody LoginRequest requestLogin) throws AuthenticationException {

        var user = userService.findByUsername(requestLogin.getUsername()).get();
        userAttemptsService.unlockWhenTimeExpired(user);
        var userAccess = limitUserService.fetchOrFailByUserId(user.getUserId()).get();

        try {
            if (userAccess.getBlockDate().isAfter(LocalDate.now())) {
                if (user.isAccountNonLocked()) {
                    if (user.isAccountNonExpired()) {
                        Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(requestLogin.getUsername(), requestLogin.getPassword()));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String jwt = jwtProvider.generateJwt(authentication);

                        userAttemptsService.onAuthenticationSuccess(requestLogin.getUsername());
                        var loginUsrDetails = new LoginResponseDetail();

                        var valueList = roleService.findAllByUser(user.getUserId()).values();
                        loginUsrDetails.setIMenuItem(valueList);

                        var userResponse = Stream.of(user)
                                .map(mapper::toUserResponse)
                                .findFirst();

                        List<Storage> storages = storageService.findAll(user.getGroup().getGroupId());
                        loginUsrDetails.setUser(userResponse.get());
                        loginUsrDetails.setToken(jwt);
                        loginUsrDetails.setStorages(storages);

                        return ResponseEntity.ok(loginUsrDetails);
                    } else {
                        throw new AccountExpiredException("Sua conta encontra-se com o acesso expirado. Por favor consultar o Administrador do sistema.");
                    }

                } else {
                    throw new AccountLockedException("Sua conta foi bloqueada devido a 3 tentativas malsucedidas."
                            + " Ele será desbloqueado após 24 horas.");
                }
            } else {
                throw new AccountAccessLimitException("Sua conta atingiu o limite de acesso. Por favor consultar o Administrador do sistema.");
            }


        } catch (BadCredentialsException e) {
            log.error("Problema ao fazer login do usuário com credenciais!", e);
            userAttemptsService.onAuthenticationFailure(requestLogin.getUsername());
            var attempts = userAttemptsService.getUserAttempts(requestLogin.getUsername()).getFailedAttempt();
            throw new BadCredentialsException("Credenciais informadas estão erradas, verifique e tente novamente."
                    + " Restam-lhe apenas " + (3 - attempts) + " tentativas. Consulte o administrador para resset de sua senha.");
        }

    }

}

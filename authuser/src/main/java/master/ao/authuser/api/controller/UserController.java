package master.ao.authuser.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.config.security.UserDetailsImpl;
import master.ao.authuser.api.mapper.UserMapper;
import master.ao.authuser.api.request.UserRequest;
import master.ao.authuser.api.response.UserResponse;
import master.ao.authuser.core.domain.model.User;
import master.ao.authuser.core.domain.service.UserService;
import master.ao.authuser.core.domain.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                          @PageableDefault(page = 0, size = 10, sort = "fullName", direction = Sort.Direction.ASC) Pageable pageable,
                                                          Authentication authentication,
                                                          @RequestParam(required = false) UUID groupId){
        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("Authentication {}",userDetails.getUsername());

        List<UserResponse> usersList = null;

        if (groupId != null) {
            usersList = userService.findAll(SpecificationTemplate.usersGroupId(groupId).and(spec))
                    .stream()
                    .map(mapper::toUserResponse)
                    .sorted((o1, o2)->o1.getFullName().
                            compareTo(o2.getFullName()))
                    .collect(Collectors.toList());
        } else {
            usersList = userService.findAll(spec)
                    .stream()
                    .map(mapper::toUserResponse)
                    .sorted((o1, o2)->o1.getFullName().
                            compareTo(o2.getFullName()))
                    .collect(Collectors.toList());
        }

        if(!usersList.isEmpty()){
            for(UserResponse user : usersList){
                //user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        int start = (int) (pageable.getOffset() > usersList.size() ? usersList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > usersList.size() ? usersList.size()
                : (start + pageable.getPageSize()));
        Page<UserResponse> usersPageList = new PageImpl<>(usersList.subList(start, end), pageable, usersList.size());

        return ResponseEntity.status(HttpStatus.OK).body(usersPageList);
    }

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getOneUser(@PathVariable(value = "userId") UUID userId){
        return userService.fetchOrFail(userId)
                .map(mapper::toUserResponse)
                .map(userResponse -> ResponseEntity.status(HttpStatus.OK).body(userResponse))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "userId") UUID userId){
        log.debug("DELETE deleteUser userId received {} ", userId);
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserRequest.UserView.UserPut.class)
                                             @JsonView(UserRequest.UserView.UserPut.class) UserRequest UserRequest){
        log.debug("PUT updateUser UserRequest received {} ", UserRequest.toString());
        return Stream.of(UserRequest)
                .map(mapper::toUser)
                .map(group -> userService.updateUser(group, userId))
                .map(mapper::toUserResponse)
                .map(userResponse -> ResponseEntity.status(HttpStatus.OK).body(userResponse))
                .findFirst()
                .get();
    }

    @PutMapping("/{userId}/password")
    @JsonView(UserRequest.UserView.PasswordPut.class)
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody UserRequest userRequest){
        log.debug("PUT updatePassword UserRequest received {} ", userRequest.toString());
        var user = mapper.toUser(userRequest);
        userService.resetPassword(userId, user);
        return ResponseEntity.status(HttpStatus.OK).body(" Senha atualizada com sucesso!");

    }




}

package master.ao.authuser.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.config.security.AuthenticationCurrentUserService;
import master.ao.authuser.api.config.security.UserDetailsImpl;
import master.ao.authuser.api.request.UserRequest;
import master.ao.authuser.core.domain.model.User;
import master.ao.authuser.core.domain.service.UserService;
import master.ao.authuser.core.domain.specifications.SpecificationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationCurrentUserService authenticationCurrentUserService;

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                  @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                  Authentication authentication){
        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("Authentication {}",userDetails.getUsername());
        Page<User> UserPage = userService.findAll(spec, pageable);
        if(!UserPage.isEmpty()){
            for(User user : UserPage.toList()){
                //user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserPage);
    }

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId){
        UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        if(currentUserId.equals(userId)) {
            Optional<User> UserOptional = userService.findById(userId);
            if (!UserOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(UserOptional.get());
            }
        }else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){
        log.debug("DELETE deleteUser userId received {} ", userId);
        Optional<User> UserOptional = userService.findById(userId);
        if(!UserOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } else{
            //userService.deleteUser(UserOptional.get());
            log.debug("DELETE deleteUser userId deleted {} ", userId);
            log.info("User deleted successfully userId {} ", userId);
            return  ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserRequest.UserView.UserPut.class)
                                             @JsonView(UserRequest.UserView.UserPut.class) UserRequest UserRequest){
        log.debug("PUT updateUser UserRequest received {} ", UserRequest.toString());
        Optional<User> UserOptional = userService.findById(userId);
        if(!UserOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } else{
           var User = UserOptional.get();
           /*  User.setFullName(UserRequest.getFullName());
            User.setPhoneNumber(UserRequest.getPhoneNumber());
            User.setCpf(UserRequest.getCpf());
            User.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.updateUser(User);
            log.debug("PUT updateUser userId saved {} ", User.getUserId());
            log.info("User updated successfully userId {} ", User.getUserId());*/
            return  ResponseEntity.status(HttpStatus.OK).body(User);
        }
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody @Validated(UserRequest.UserView.PasswordPut.class)
                                                 @JsonView(UserRequest.UserView.PasswordPut.class) UserRequest UserRequest){
        log.debug("PUT updatePassword UserRequest received {} ", UserRequest.toString());
        Optional<User> UserOptional = userService.findById(userId);
        if(!UserOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } if(!UserOptional.get().getPassword().equals(UserRequest.getOldPassword())){
            log.warn("Mismatched old password userId {} ", userId);
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        } else{
            var User = UserOptional.get();
            User.setPassword(UserRequest.getPassword());
            User.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            //userService.updatePassword(User);
            log.debug("PUT updatePassword userId saved {} ", User.getUserId());
            log.info("Password updated successfully userId {} ", User.getUserId());
            return  ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
        }
    }

   /* @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody @Validated(UserRequest.UserView.ImagePut.class)
                                              @JsonView(UserRequest.UserView.ImagePut.class) UserRequest UserRequest){
        log.debug("PUT updateImage UserRequest received {} ", UserRequest.toString());
        Optional<User> UserOptional = userService.findById(userId);
        if(!UserOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } else{
            var User = UserOptional.get();
            //User.setImageUrl(UserRequest.getImageUrl());
            User.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.updateUser(User);
            log.debug("PUT updateImage userId saved {} ", User.getUserId());
            log.info("Image updated successfully userId {} ", User.getUserId());
            return  ResponseEntity.status(HttpStatus.OK).body(User);
        }
    }*/



}

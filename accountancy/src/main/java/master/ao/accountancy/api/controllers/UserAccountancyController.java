package master.ao.accountancy.api.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.accountancy.api.mapper.UserMapper;
import master.ao.accountancy.api.requests.UserRequest;
import master.ao.accountancy.domain.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Log4j2
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserAccountancyController {

    private final UserService userService;
    private final UserMapper mapper;

    @PostMapping()
    public ResponseEntity<?> saveUserToAccountancy(@RequestBody UserRequest request) {
        var user = mapper.toUser(request);
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

}
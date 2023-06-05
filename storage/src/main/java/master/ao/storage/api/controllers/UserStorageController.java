package master.ao.storage.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.mapper.UserMapper;
import master.ao.storage.api.request.UserRequest;
import master.ao.storage.core.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserStorageController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper mapper;

    @PostMapping()
    public ResponseEntity<?> saveUserToStorage(@RequestBody UserRequest request) {
        var user = mapper.toUser(request);
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

}
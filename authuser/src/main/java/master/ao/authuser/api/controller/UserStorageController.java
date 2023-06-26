package master.ao.authuser.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.core.domain.model.Storage;
import master.ao.authuser.core.domain.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class UserStorageController {

    @Autowired
    StorageService storageService;

    @PostMapping
    public ResponseEntity<?> saveUserToStorage(@Valid @RequestBody Storage request) {
        storageService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @GetMapping("/{userGroup}")
    public ResponseEntity<List<Storage>> saveUserToStorage(@PathVariable UUID userGroup) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(storageService.findAll(userGroup));
    }

    @DeleteMapping("/{userGroup}")
    public ResponseEntity<?> deleteStorage(@PathVariable UUID userGroup) {
        storageService.delete(userGroup);
        return ResponseEntity.status(HttpStatus.OK)
                .body("");
    }

}
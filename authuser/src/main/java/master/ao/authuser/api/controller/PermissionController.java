package master.ao.authuser.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.mapper.PermissionMapper;
import master.ao.authuser.api.request.PermissionRequest;
import master.ao.authuser.api.response.PermissionResponse;
import master.ao.authuser.core.domain.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/permission")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PermissionController {

    private final PermissionService permissionService;
    private final PermissionMapper mapper;

    @PostMapping("/{groupId}/group")
    public ResponseEntity<PermissionResponse> savePermission(@Valid @RequestBody PermissionRequest request, @PathVariable("groupId") UUID groupId){
        log.debug("POST createPermission request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toPermission)
                .map(permission -> permissionService.save(permission))
                .map(mapper::toPermissionResponse)
                .map(PermissionResponse -> ResponseEntity.status(HttpStatus.CREATED).body(PermissionResponse))
                .findFirst()
                .get();
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionResponse> updatePermission(@Valid @RequestBody  PermissionRequest request,
                                                     @PathVariable("permissionId") UUID permissionId){
        log.debug("PUT updatePermission request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toPermission)
                .map(permission -> permissionService.update(permission, permissionId))
                .map(mapper::toPermissionResponse)
                .map(permissionResponse -> ResponseEntity.status(HttpStatus.OK).body(permissionResponse))
                .findFirst()
                .get();
    }


    @GetMapping("/{PermissionId}")
    public ResponseEntity<PermissionResponse> fetchOrFail(@PathVariable("permissionId") UUID PermissionId) {
        return permissionService.fetchOrFail(PermissionId)
                .map(mapper::toPermissionResponse)
                .map(permissionResponse -> ResponseEntity.status(HttpStatus.OK).body(permissionResponse))
                .orElse(ResponseEntity.notFound().build());

    }


}

package master.ao.authuser.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.mapper.PermissionMapper;
import master.ao.authuser.api.request.PermissionRequest;
import master.ao.authuser.api.response.PermissionResponse;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@Tag(name = "Permission", description = "The Permission API. Contains all operations that can be performed on a group")
public class PermissionController {

    private final PermissionService permissionService;
    private final PermissionMapper mapper;

    @Operation(summary = "Create permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permission created!",
                    content = @Content(schema = @Schema(implementation = PermissionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping("/{groupId}/group")
    public ResponseEntity<PermissionResponse> savePermission(@Valid @RequestBody PermissionRequest request,
                                                             @Parameter(description = "id of the group that belongs to the permission") @PathVariable("groupId") UUID groupId){
        log.debug("POST createPermission request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toPermission)
                .map(permission -> permissionService.save(permission))
                .map(mapper::toPermissionResponse)
                .map(PermissionResponse -> ResponseEntity.status(HttpStatus.CREATED).body(PermissionResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Update permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permission updated!",
                    content = @Content(schema = @Schema(implementation = PermissionResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Permission not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionResponse> updatePermission(@Valid @RequestBody  PermissionRequest request,
                                                               @Parameter(description = "id of permission to be updated")  @PathVariable("permissionId") UUID permissionId){
        log.debug("PUT updatePermission request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toPermission)
                .map(permission -> permissionService.update(permission, permissionId))
                .map(mapper::toPermissionResponse)
                .map(permissionResponse -> ResponseEntity.status(HttpStatus.OK).body(permissionResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get a permission by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the permission",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = PermissionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Permission not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{PermissionId}")
    public ResponseEntity<PermissionResponse> fetchOrFail(@PathVariable("permissionId") UUID PermissionId) {
        return permissionService.fetchOrFail(PermissionId)
                .map(mapper::toPermissionResponse)
                .map(permissionResponse -> ResponseEntity.status(HttpStatus.OK).body(permissionResponse))
                .orElse(ResponseEntity.notFound().build());

    }


}

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
import master.ao.authuser.api.mapper.RoleMapper;
import master.ao.authuser.api.request.RoleRequest;
import master.ao.authuser.api.response.RoleResponse;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.service.RoleService;
import master.ao.authuser.core.domain.specifications.SpecificationTemplate;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
@Tag(name = "Role", description = "The Role API. Contains all operations that can be performed on a Role")
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper mapper;

    @Operation(summary = "Create role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created!",
                    content = @Content(schema = @Schema(implementation = RoleResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping("/{permissionId}/permission")
    public ResponseEntity<RoleResponse> saveRole(@Valid @RequestBody RoleRequest request,
                                                 @Parameter(description = "id of permission to be associated with the role") @PathVariable("permissionId") UUID permissionId){
        log.debug("POST createRole request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toRole)
                .map(role -> roleService.save(role, permissionId))
                .map(mapper::toRoleResponse)
                .map(RoleResponse -> ResponseEntity.status(HttpStatus.CREATED).body(RoleResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Update role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated role", content = @Content(schema = @Schema(implementation = RoleResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "System error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody  RoleRequest request,
                                                   @Parameter(description = "id of role to be updated")  @PathVariable("roleId") UUID roleId){
        log.debug("PUT updateRole request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toRole)
                .map(role -> roleService.update(role, roleId))
                .map(mapper::toRoleResponse)
                .map(RoleResponse -> ResponseEntity.status(HttpStatus.OK).body(RoleResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Roles",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<RoleResponse>> getAll(@ParameterObject SpecificationTemplate.RoleSpec spec,
                                                     @ParameterObject @PageableDefault(page = 0, size = 10, sort = "description", direction = Sort.Direction.ASC) Pageable pageable,
                                                     @Parameter(description = "id of permission associate with the role") @RequestParam(required = false) UUID permissionId) {

        List<RoleResponse> rolesList = new ArrayList<>();

        if (permissionId != null) {
            rolesList = roleService.findAll(SpecificationTemplate.rolePermissionId(permissionId).and(spec))
                    .stream()
                    .map(mapper::toRoleResponse)
                    .collect(Collectors.toList());
        } else {
            rolesList = roleService.findAll(spec)
                    .stream()
                    .map(mapper::toRoleResponse)
                    .collect(Collectors.toList());
        }


        int start = (int) (pageable.getOffset() > rolesList.size() ? rolesList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > rolesList.size() ? rolesList.size()
                : (start + pageable.getPageSize()));
        Page<RoleResponse> rolesPageList = new PageImpl<>(rolesList.subList(start, end), pageable, rolesList.size());

        return ResponseEntity.status(HttpStatus.OK).body(rolesPageList);

    }

    @Operation(summary = "Get a role by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the role",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponse> fetchOrFail(@PathVariable("roleId") UUID roleId) {
        return roleService.fetchOrFail(roleId)
                .map(mapper::toRoleResponse)
                .map(RoleResponse -> ResponseEntity.status(HttpStatus.OK).body(RoleResponse))
                .orElse(ResponseEntity.notFound().build());

    }


}

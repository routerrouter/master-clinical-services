package master.ao.authuser.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import master.ao.authuser.api.mapper.RoleMapper;
import master.ao.authuser.api.response.RoleResponse;
import master.ao.authuser.api.response.UserRoleAccesses;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.model.Role;
import master.ao.authuser.core.domain.model.User;
import master.ao.authuser.core.domain.service.RoleService;
import master.ao.authuser.core.domain.service.UserService;
import master.ao.authuser.core.domain.specifications.SpecificationTemplate;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role_user")
@Tag(name = "Roles User", description = "The Roles User API. Contains all operations that can be performed on a Roles User")
public class UserRolesController {

    private final RoleService roleService;
    private final UserService userService;
    private final RoleMapper mapper;


    @Operation(summary = "Get all roles of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found roles of user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping()
    public ResponseEntity<Page<RoleResponse>> getAll(@ParameterObject SpecificationTemplate.RoleSpec spec,
                                                     @ParameterObject @PageableDefault(page = 0, size = 10, sort = "description", direction = Sort.Direction.ASC) Pageable pageable,
                                                     @Parameter(description = "id of associated user") @RequestParam(required = false) UUID userId) {

        List<RoleResponse> roleResponseList = roleResponseList = roleService.findAll(SpecificationTemplate.roleUserId(userId).and(spec))
                .stream()
                .map(mapper::toRoleResponse)
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > roleResponseList.size() ? roleResponseList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > roleResponseList.size() ? roleResponseList.size()
                : (start + pageable.getPageSize()));
        Page<RoleResponse> responsePage = new PageImpl<>(roleResponseList.subList(start, end), pageable, roleResponseList.size());

        return ResponseEntity.status(HttpStatus.OK).body(responsePage);

    }

    @GetMapping("/access")
    public ResponseEntity<?> getAllAccessUser(@ParameterObject SpecificationTemplate.RoleSpec spec,
                                              @RequestParam(required = false) UUID userId) {

        Optional<User> userOptional = userService.fetchOrFail(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário informado não foi encontrado!");
        }
        List<Role> roleList = roleService.findAll(SpecificationTemplate.roleUserId(userId).and(spec));
        List<UserRoleAccesses> userRoleAccssesList = new ArrayList<>();

        Map<String, List<Role>> listMap =
                roleList.stream()
                        .collect(Collectors.groupingBy(permission -> permission.getPermission().getDescription()));

        Map<String, Object> response = new HashMap<>();
        //listMap.forEach((permission, roles) -> userRoleAccssesList.add(new UserRoleAccsses(permission, roles)));
        response.put("access", userRoleAccssesList);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping("/{userId}/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associateRolesIntoUser(@RequestBody List<UUID> roles,
                                                       @PathVariable("userId") UUID userId) {
        userService.associateRoles(userId, roles);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removeRoles(@PathVariable UUID userId, @RequestBody List<UUID> roles) {
        userService.removeRoles(userId, roles);
        return ResponseEntity.noContent().build();
    }


}

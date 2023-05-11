package master.ao.authuser.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.mapper.RoleMapper;
import master.ao.authuser.api.response.RoleResponse;
import master.ao.authuser.api.response.UserRoleAccsses;
import master.ao.authuser.core.domain.model.Role;
import master.ao.authuser.core.domain.model.User;
import master.ao.authuser.core.domain.service.RoleService;
import master.ao.authuser.core.domain.service.UserService;
import master.ao.authuser.core.domain.specifications.SpecificationTemplate;
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

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/role_user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserRolesController {

    private final RoleService roleService;
    private final UserService userService;
    private final RoleMapper mapper;

    @GetMapping()
    public ResponseEntity<Page<RoleResponse>> getAll(SpecificationTemplate.RoleSpec spec,
                                                     @PageableDefault(page = 0, size = 10, sort = "description", direction = Sort.Direction.ASC) Pageable pageable,
                                                     @RequestParam(required = false) UUID userId) {

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
    public ResponseEntity<?> getAllAccessUser(SpecificationTemplate.RoleSpec spec,
                                                                 @RequestParam(required = false) UUID userId) {

        Optional<User> userOptional = userService.fetchOrFail(userId);
        if(!userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário informado não foi encontrado!");
        }
        List<Role> roleList = roleService.findAll(SpecificationTemplate.roleUserId(userId).and(spec));
        List<UserRoleAccsses> userRoleAccssesList = new ArrayList<>();

        Map<String, List<Role>> listMap =
                roleList.stream()
                        .collect(Collectors.groupingBy(permission -> permission.getPermission().getDescription()));

        Map<String, Object> response = new HashMap<>();
        listMap.forEach((permission, roles) -> userRoleAccssesList.add(new UserRoleAccsses(permission, roles)));
        response.put("access", userRoleAccssesList);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping("/{userId}/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associateRolesIntoUser(@RequestBody List<UUID> roles,
                                                                  @PathVariable("userId") UUID userId){
        userService.associateRoles(userId,roles);
        return ResponseEntity.noContent().build();
    }

    //@CheckSecurity.UsuariosgroupsPermissoes.PodeEditar
    @DeleteMapping("/{userId}/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removeRoles(@PathVariable UUID userId, @RequestBody List<UUID> roles) {
        userService.removeRoles(userId,roles);
        return ResponseEntity.noContent().build();
    }

    


}

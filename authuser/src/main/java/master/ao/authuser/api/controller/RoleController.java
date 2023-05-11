package master.ao.authuser.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.mapper.RoleMapper;
import master.ao.authuser.api.request.RoleRequest;
import master.ao.authuser.api.response.RoleResponse;
import master.ao.authuser.core.domain.service.RoleService;
import master.ao.authuser.core.domain.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper mapper;

    @PostMapping("/{permissionId}/permission")
    public ResponseEntity<RoleResponse> saveRole(@Valid @RequestBody RoleRequest request,
                                                 @PathVariable("permissionId") UUID permissionId){
        log.debug("POST createRole request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toRole)
                .map(role -> roleService.save(role, permissionId))
                .map(mapper::toRoleResponse)
                .map(RoleResponse -> ResponseEntity.status(HttpStatus.CREATED).body(RoleResponse))
                .findFirst()
                .get();
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody  RoleRequest request,
                                                     @PathVariable("roleId") UUID roleId){
        log.debug("PUT updateRole request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toRole)
                .map(role -> roleService.update(role, roleId))
                .map(mapper::toRoleResponse)
                .map(RoleResponse -> ResponseEntity.status(HttpStatus.OK).body(RoleResponse))
                .findFirst()
                .get();
    }

    @GetMapping
    public ResponseEntity<Page<RoleResponse>> getAll(SpecificationTemplate.RoleSpec spec,
                                                     @PageableDefault(page = 0, size = 10, sort = "description", direction = Sort.Direction.ASC) Pageable pageable,
                                                     @RequestParam(required = false) UUID permissionId) {

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

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponse> fetchOrFail(@PathVariable("roleId") UUID roleId) {
        return roleService.fetchOrFail(roleId)
                .map(mapper::toRoleResponse)
                .map(RoleResponse -> ResponseEntity.status(HttpStatus.OK).body(RoleResponse))
                .orElse(ResponseEntity.notFound().build());

    }


}

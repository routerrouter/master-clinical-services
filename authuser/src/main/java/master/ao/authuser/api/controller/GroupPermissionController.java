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
import master.ao.authuser.api.mapper.GroupMapper;
import master.ao.authuser.api.mapper.PermissionMapper;
import master.ao.authuser.api.request.GroupRequest;
import master.ao.authuser.api.request.PermissionsGroupRequest;
import master.ao.authuser.api.response.GroupResponse;
import master.ao.authuser.api.response.PermissionResponse;
import master.ao.authuser.api.response.RoleResponse;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.model.Permission;
import master.ao.authuser.core.domain.service.GroupService;
import master.ao.authuser.core.domain.service.PermissionService;
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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/group_permissions")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Permissions Group", description = "The Permissions group API. Contains all operations that can be performed on a Permissions group.")
public class GroupPermissionController {

    private final GroupService groupService;
    private final PermissionService permissionService;
    private final PermissionMapper mapper;

    @Operation(summary = "Get all permissions of group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Permissions",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping()
    public ResponseEntity<Page<PermissionResponse>> getAll(@ParameterObject SpecificationTemplate.PermissionSpec spec,
                                                           @ParameterObject  @PageableDefault(page = 0, size = 10, sort = "description", direction = Sort.Direction.ASC) Pageable pageable,
                                                           @RequestParam(required = false) UUID groupId) {

        List<PermissionResponse> permissionResponseList = new ArrayList<>();

        if (groupId != null) {
            permissionResponseList = permissionService.findAll(SpecificationTemplate.permissionGroupId(groupId).and(spec))
                    .stream()
                    .map(mapper::toPermissionResponse)
                    .collect(Collectors.toList());
        } else {
            permissionResponseList = permissionService.findAll(spec)
                    .stream()
                    .map(mapper::toPermissionResponse)
                    .collect(Collectors.toList());
        }


        int start = (int) (pageable.getOffset() > permissionResponseList.size() ? permissionResponseList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > permissionResponseList.size() ? permissionResponseList.size()
                : (start + pageable.getPageSize()));
        Page<PermissionResponse> responsePage = new PageImpl<>(permissionResponseList.subList(start, end), pageable, permissionResponseList.size());

        return ResponseEntity.status(HttpStatus.OK).body(responsePage);

    }


    @Operation(summary = "Save permissions into group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permissions associated!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping("/{groupId}/group")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> savePermissionsIntoGroup(@RequestBody List<UUID> permissions,
                                                           @Parameter(description = "id of group to be associated")  @PathVariable("groupId") UUID groupId){
        groupService.associatePermissions(groupId,permissions);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Delete association between permission and group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted association!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Group or permission not found",
                    content = @Content)})
    @DeleteMapping("/{groupId}/group")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> disassociate(@PathVariable UUID groupId, @RequestBody List<UUID> permissions) {
        groupService.disassociatePermission(groupId, permissions);
        return ResponseEntity.noContent().build();
    }

    


}

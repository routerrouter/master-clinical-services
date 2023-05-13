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
import master.ao.authuser.api.request.GroupRequest;
import master.ao.authuser.api.response.GroupResponse;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.service.GroupService;
import master.ao.authuser.core.domain.specifications.SpecificationTemplate;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
@Tag(name = "Group", description = "The Group API. Contains all operations that can be performed on a group")
public class GroupController {

    private final GroupService groupService;
    private final GroupMapper mapper;

    @Operation(summary = "Create group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Group created!",
                    content = @Content(schema = @Schema(implementation = GroupResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping
    public ResponseEntity<GroupResponse> saveGroup(@Valid @RequestBody GroupRequest request) {
        log.debug("POST createGroup request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toGroup)
                .map(groupService::save)
                .map(mapper::toGroupResponse)
                .map(groupResponse -> ResponseEntity.status(HttpStatus.CREATED).body(groupResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated group", content = @Content(schema = @Schema(implementation = GroupResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Group not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{groupId}")
    public ResponseEntity<GroupResponse> updateGroup(@Valid @RequestBody GroupRequest request,
                                                     @Parameter(description = "id of group to be updated") @PathVariable("groupId") UUID groupId) {
        log.debug("PUT updateGroup request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toGroup)
                .map(group -> groupService.update(group, groupId))
                .map(mapper::toGroupResponse)
                .map(groupResponse -> ResponseEntity.status(HttpStatus.OK).body(groupResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Get all groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Groups",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAll(@ParameterObject SpecificationTemplate.GroupSpec spec) {
        var groupsList = groupService.findAll(spec)
                .stream()
                .map(mapper::toGroupResponse)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(groupsList);

    }

    @Operation(summary = "Get a group by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the group",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = GroupResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Group not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> fetchOrFail(@Parameter(description = "id of group to be searched") @PathVariable("groupId") UUID groupId) {
        return groupService.fetchOrFail(groupId)
                .map(mapper::toGroupResponse)
                .map(groupResponse -> ResponseEntity.status(HttpStatus.OK).body(groupResponse))
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted group!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Group not found",
                    content = @Content)})
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Parameter(description = "id of group to be deleted") @PathVariable UUID grupoId) {
        groupService.delete(grupoId);
    }


}

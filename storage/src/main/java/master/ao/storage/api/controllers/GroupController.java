package master.ao.storage.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.config.security.UserDetailsImpl;
import master.ao.storage.api.mapper.GroupMapper;
import master.ao.storage.api.request.GroupRequest;
import master.ao.storage.api.response.EntityResponse;
import master.ao.storage.api.response.GroupResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.services.GroupService;
import master.ao.storage.core.domain.specifications.SpecificationTemplate;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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


    @Operation(summary = "Create Group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Group created!",
                    content = @Content(schema = @Schema(implementation = GroupResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping()
    public ResponseEntity<GroupResponse> saveGroup(@Valid @RequestBody GroupRequest request,
                                                   Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("POST createGroup request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toGroup)
                .map(group -> groupService.save(group, userDetails.getUserId()))
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
                                                     @PathVariable("groupId") UUID groupId) {
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
    public ResponseEntity<Page<GroupResponse>> getAll(@ParameterObject SpecificationTemplate.GroupSpec spec,
                                                      @ParameterObject @PageableDefault(page = 0, size = 10, sort = "groupId", direction = Sort.Direction.ASC) Pageable pageable) {
        var groupsList = groupService.findAll(spec)
                .stream()
                .map(mapper::toGroupResponse)
                .sorted((o1, o2) -> o1.getName().
                        compareTo(o2.getName()))
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > groupsList.size() ? groupsList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > groupsList.size() ? groupsList.size()
                : (start + pageable.getPageSize()));
        Page<GroupResponse> groupsPageList = new PageImpl<>(groupsList.subList(start, end), pageable, groupsList.size());

        return ResponseEntity.status(HttpStatus.OK).body(groupsPageList);

    }

    @Operation(summary = "Get a group by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the group",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GroupResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Group not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> fetchOrFail(@PathVariable("groupId") UUID groupId) {
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
    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable UUID groupId) {
        groupService.delete(groupId);
    }


}

package master.ao.storage.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.mapper.GroupMapper;
import master.ao.storage.api.request.GroupRequest;
import master.ao.storage.api.response.GroupResponse;
import master.ao.storage.core.domain.services.GroupService;
import master.ao.storage.core.domain.specifications.SpecificationTemplate;
import org.springframework.http.HttpStatus;
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

public class GroupController {

    private final GroupService groupService;
    private final GroupMapper mapper;


    @PostMapping()
    public ResponseEntity<GroupResponse> saveGroup(@Valid @RequestBody GroupRequest request,
                                                   Authentication authentication) {
        System.out.println(authentication.getPrincipal());
        log.debug("POST createGroup request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toGroup)
                .map(group -> groupService.save(group))
                .map(mapper::toGroupResponse)
                .map(groupResponse -> ResponseEntity.status(HttpStatus.CREATED).body(groupResponse))
                .findFirst()
                .get();
    }


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


    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAll(SpecificationTemplate.GroupSpec spec) {
        var groupsList = groupService.findAll(spec)
                .stream()
                .map(mapper::toGroupResponse)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(groupsList);

    }


    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> fetchOrFail(@PathVariable("groupId") UUID groupId) {
        return groupService.fetchOrFail(groupId)
                .map(mapper::toGroupResponse)
                .map(groupResponse -> ResponseEntity.status(HttpStatus.OK).body(groupResponse))
                .orElse(ResponseEntity.notFound().build());

    }


    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable UUID groupId) {
        groupService.delete(groupId);
    }


}

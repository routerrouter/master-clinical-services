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
import master.ao.authuser.api.mapper.AccessLimitUserMapper;
import master.ao.authuser.api.request.AccessLimitRequest;
import master.ao.authuser.api.response.AccessLimitResponse;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.service.AccessLimitUserService;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/accessLimit")
@Tag(name = "AccessLimitUser", description = "The AccessLimitUser API. Contains all operations that can be performed in access limit control")
public class AccessLimitUserController {

    private final AccessLimitUserMapper mapper;
    private final AccessLimitUserService service;


    @Operation(summary = "Create AccessLimitUser")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "AccessLimitUser created!",
                    content = @Content(schema = @Schema(implementation = AccessLimitResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping("/{userId}/user")
    public ResponseEntity<AccessLimitResponse> saveAccessLimit(@Valid @RequestBody AccessLimitRequest request,
                                                               @Parameter(description = "id user to be associated with the access limit") @PathVariable(value = "userId") UUID userId){
        log.debug("POST saveAccessLimit request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toAcessLimitUser)
                .map(limit -> service.save(limit,userId))
                .map(mapper::toAccessLimitResponse)
                .map(accessLimitUserResponse -> ResponseEntity.status(HttpStatus.CREATED).body(accessLimitUserResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Update AccessLimitUser")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AccessLimitUser updated!",
                    content = @Content(schema = @Schema(implementation = AccessLimitResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{userId}/user")
    public ResponseEntity<AccessLimitResponse> updateAccessLimit(@Parameter(description = "id user to be searched") @PathVariable(value = "userId") UUID userId,
                                                                 @Valid @RequestBody AccessLimitRequest request){
        log.debug("PUT updateAccessLimit AccessLimitRequest received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toAcessLimitUser)
                .map(limitUser -> service.update(limitUser, userId))
                .map(mapper::toAccessLimitResponse)
                .map(accessLimitUserResponse -> ResponseEntity.status(HttpStatus.OK).body(accessLimitUserResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all accessLimits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found AccessLimitUsers",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping()
    public ResponseEntity<Page<AccessLimitResponse>> getAll(@ParameterObject
                                                                @PageableDefault(page = 0, size = 10, sort = "ativation", direction = Sort.Direction.DESC) Pageable pageable) {

        List<AccessLimitResponse> accessLimitResponses = service.findAll()
                    .stream()
                    .map(mapper::toAccessLimitResponse)
                    .collect(Collectors.toList());


        int start = (int) (pageable.getOffset() > accessLimitResponses.size() ? accessLimitResponses.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > accessLimitResponses.size() ? accessLimitResponses.size()
                : (start + pageable.getPageSize()));
        Page<AccessLimitResponse> responsePage = new PageImpl<>(accessLimitResponses.subList(start, end), pageable, accessLimitResponses.size());

        return ResponseEntity.status(HttpStatus.OK).body(responsePage);

    }

    @Operation(summary = "Get a accessLimitUser by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the AccessLimitUser",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = AccessLimitResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "AccessLimitUser not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<AccessLimitResponse> findByUserId(@Parameter(description = "id user to be searched") @PathVariable(value = "userId") UUID userId){
        return service.fetchOrFailByUserId(userId)
                .map(mapper::toAccessLimitResponse)
                .map(accessLimitUserResponse -> ResponseEntity.status(HttpStatus.OK).body(accessLimitUserResponse))
                .orElse(ResponseEntity.notFound().build());
    }

}

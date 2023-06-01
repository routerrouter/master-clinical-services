package master.ao.authuser.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.mapper.UserMapper;
import master.ao.authuser.api.request.UserRequest;
import master.ao.authuser.api.response.UserResponse;
import master.ao.authuser.core.domain.exception.BussinessException;
import master.ao.authuser.core.domain.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "The User API. Contains all operations that can be performed on a user.")
public class UserController {


    private final UserService userService;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;


    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Users",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(@ParameterObject SpecificationTemplate.UserSpec spec,
                                                          @ParameterObject @PageableDefault(page = 0, size = 10, sort = "fullName", direction = Sort.Direction.ASC) Pageable pageable,
                                                          @Parameter(description = "id of group which associated") @RequestParam(required = false) UUID groupId) {

        List<UserResponse> usersList = new ArrayList<>();

        if (groupId != null) {
            usersList = userService.findAll(SpecificationTemplate.usersGroupId(groupId).and(spec))
                    .stream()
                    .map(mapper::toUserResponse)
                    .sorted((o1, o2) -> o1.getFullName().
                            compareTo(o2.getFullName()))
                    .collect(Collectors.toList());
        } else {
            usersList = userService.findAll(spec)
                    .stream()
                    .map(mapper::toUserResponse)
                    .sorted((o1, o2) -> o1.getFullName().
                            compareTo(o2.getFullName()))
                    .collect(Collectors.toList());
        }

        int start = (int) (pageable.getOffset() > usersList.size() ? usersList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > usersList.size() ? usersList.size()
                : (start + pageable.getPageSize()));
        Page<UserResponse> usersPageList = new PageImpl<>(usersList.subList(start, end), pageable, usersList.size());

        return ResponseEntity.status(HttpStatus.OK).body(usersPageList);
    }


    @Operation(summary = "Get a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getOneUser(@Parameter(description = "id of user to be searched") @PathVariable(value = "userId") UUID userId) {
        return userService.fetchOrFail(userId)
                .map(mapper::toUserResponse)
                .map(userResponse -> ResponseEntity.status(HttpStatus.OK).body(userResponse))
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted user!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Parameter(description = "id of user to be deleted") @PathVariable(value = "userId") UUID userId) {
        log.debug("DELETE deleteUser userId received {} ", userId);
        userService.deleteUser(userId);
    }


    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated user", content = @Content(schema = @Schema(implementation = UserResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@Parameter(description = "id of user to be updated") @PathVariable(value = "userId") UUID userId,
                                                   @RequestBody @Validated(UserRequest.UserView.UserPut.class)
                                                   @JsonView(UserRequest.UserView.UserPut.class) UserRequest UserRequest,
                                                   @RequestHeader("Authorization") String token) {
        log.debug("PUT updateUser UserRequest received {} ", UserRequest.toString());
        return Stream.of(UserRequest)
                .map(mapper::toUser)
                .map(user -> userService.updateUser(user, userId, token))
                .map(mapper::toUserResponse)
                .map(userResponse -> ResponseEntity.status(HttpStatus.OK).body(userResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated!", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{userId}/password")
    @JsonView(UserRequest.UserView.PasswordPut.class)
    public ResponseEntity<Object> updatePassword(@Parameter(description = "id of user to be updated") @PathVariable(value = "userId") UUID userId,
                                                 @RequestBody UserRequest userRequest) {
        log.debug("PUT updatePassword UserRequest received {} ", userRequest.toString());
        var user = mapper.toUser(userRequest);
        userService.resetPassword(userId, user);
        return ResponseEntity.status(HttpStatus.OK).body(" Senha atualizada com sucesso!");

    }


}

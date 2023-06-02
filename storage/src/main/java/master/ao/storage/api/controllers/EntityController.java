package master.ao.storage.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.config.security.UserDetailsImpl;
import master.ao.storage.api.mapper.EntityMapper;
import master.ao.storage.api.request.EntityRequest;
import master.ao.storage.api.response.EntityResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.services.EntityService;
import master.ao.storage.core.domain.specifications.SpecificationTemplate;
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
@RequestMapping("/entity")
@Tag(name = "Entity", description = "The Entities API. Contains all operations that can be performed on a Entity")
public class EntityController {

    private final EntityService entityService;
    private final EntityMapper mapper;


    @Operation(summary = "Create entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entity created!",
                    content = @Content(schema = @Schema(implementation = EntityResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping()
    public ResponseEntity<EntityResponse> saveEntity(@Valid @RequestBody EntityRequest request,
                                                   Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("POST createEntity request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toEntity)
                .map(entity -> entityService.saveEndCreateAccount(entity, userDetails.getUserId()))
                .map(mapper::toEntityResponse)
                .map(entityResponse -> ResponseEntity.status(HttpStatus.CREATED).body(entityResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated entity", content = @Content(schema = @Schema(implementation = EntityResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{entityId}")
    public ResponseEntity<EntityResponse> updateEntity(@Valid @RequestBody EntityRequest request,
                                                       @Parameter(description = "id of entity to be updated") @PathVariable("entityId") UUID entityId) {
        log.debug("PUT updateEntity request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toEntity)
                .map(entity -> entityService.updateAndUpdateAccount(entity, entityId))
                .map(mapper::toEntityResponse)
                .map(entityResponse -> ResponseEntity.status(HttpStatus.OK).body(entityResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Categories",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<EntityResponse>> getAll(SpecificationTemplate.EntitySpec spec,
                                                      @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        List<EntityResponse> entitiesList = entityService.findAll(spec)
                .stream()
                .map(mapper::toEntityResponse)
                .sorted((o1, o2) -> o1.getName().
                        compareTo(o2.getName()))
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > entitiesList.size() ? entitiesList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > entitiesList.size() ? entitiesList.size()
                : (start + pageable.getPageSize()));
        Page<EntityResponse> entitiesPageList = new PageImpl<>(entitiesList.subList(start, end), pageable, entitiesList.size());

        return ResponseEntity.status(HttpStatus.OK).body(entitiesPageList);

    }

    @Operation(summary = "Get a entity by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the entity",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EntityResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{entityId}")
    public ResponseEntity<EntityResponse> fetchOrFail(@Parameter(description = "id of entity to be searched")
                                                          @PathVariable("entityId") UUID entityId) {
        return entityService.fetchOrFail(entityId)
                .map(mapper::toEntityResponse)
                .map(entityResponse -> ResponseEntity.status(HttpStatus.OK).body(entityResponse))
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete a entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted entity!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content)})
    @DeleteMapping("/{entityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Parameter(description = "id of entity to be deleted") @PathVariable UUID entityId) {
        entityService.delete(entityId);
    }


}

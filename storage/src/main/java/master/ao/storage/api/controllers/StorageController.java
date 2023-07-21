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
import master.ao.storage.api.mapper.StorageMapper;
import master.ao.storage.api.request.StorageRequest;
import master.ao.storage.api.response.StorageResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.services.StorageService;
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
@RequestMapping("/storage")
@Tag(name = "Storage", description = "The Storage API. Contains all operations that can be performed on a Storage")
public class StorageController {

    private final StorageService storageService;
    private final StorageMapper mapper;

    @Operation(summary = "Create storage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Storage created!",
                    content = @Content(schema = @Schema(implementation = StorageResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping()
    public ResponseEntity<StorageResponse> saveStorage(@Valid @RequestBody StorageRequest request,
                                                       Authentication authentication,
                                                       @RequestHeader("Authorization") String token) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("POST createStorage request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toStorage)
                .map(storage -> storageService.save(storage, userDetails.getUserId(), token))
                .map(mapper::toStorageResponse)
                .map(StorageResponse -> ResponseEntity.status(HttpStatus.CREATED).body(StorageResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update storage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated storage", content = @Content(schema = @Schema(implementation = StorageResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Storage not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{storageId}")
    public ResponseEntity<StorageResponse> updateStorage(@Valid @RequestBody StorageRequest request,
                                                         @Parameter(description = "id of storage to be updated")
                                                         @PathVariable("storageId") UUID storageId,
                                                         @RequestHeader("Authorization") String token) {
        log.debug("PUT updateStorage request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toStorage)
                .map(storage -> storageService.update(storage, storageId, token))
                .map(mapper::toStorageResponse)
                .map(StorageResponse -> ResponseEntity.status(HttpStatus.OK).body(StorageResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Get all storages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Storages",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})

    @GetMapping
    public ResponseEntity<Page<StorageResponse>> getAll(@ParameterObject SpecificationTemplate.StorageSpec spec,
                                                        @PageableDefault(page = 0, size = 10, sort = "storageId", direction = Sort.Direction.ASC) Pageable pageable) {
        var storagesList = storageService.findAll(spec)
                .stream()
                .map(mapper::toStorageResponse)
                .sorted((o1, o2) -> o1.getName().
                        compareTo(o2.getName()))
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > storagesList.size() ? storagesList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > storagesList.size() ? storagesList.size()
                : (start + pageable.getPageSize()));
        Page<StorageResponse> storagesPageList = new PageImpl<>(storagesList.subList(start, end), pageable, storagesList.size());

        return ResponseEntity.status(HttpStatus.OK).body(storagesPageList);

    }


    @Operation(summary = "Get a storage by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the storage",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StorageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Storage not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{storageId}")
    public ResponseEntity<StorageResponse> fetchOrFail(@Parameter(description = "id of storage to be searched")
                                                           @PathVariable("storageId") UUID storageId) {
        return storageService.fetchOrFail(storageId)
                .map(mapper::toStorageResponse)
                .map(storageResponse -> ResponseEntity.status(HttpStatus.OK).body(storageResponse))
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete a storage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted storage!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Storage not found",
                    content = @Content)})
    @DeleteMapping("/{storageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Parameter(description = "id of storage to be deleted")
                           @PathVariable UUID storageId,
                       @RequestHeader("Authorization") String token) {
        storageService.delete(storageId, token);
    }


}

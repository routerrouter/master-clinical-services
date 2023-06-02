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
import master.ao.storage.api.mapper.LocationMapper;
import master.ao.storage.api.request.LocationRequest;
import master.ao.storage.api.response.LocationResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.services.LocationService;
import master.ao.storage.core.domain.specifications.SpecificationTemplate;
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
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
@Tag(name = "Location", description = "The Location API. Contains all operations that can be performed on a location")
public class LocationController {

    private final LocationService locationService;
    private final LocationMapper mapper;

    @Operation(summary = "Create location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Location created!",
                    content = @Content(schema = @Schema(implementation = LocationResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})

    @PostMapping("/{storageId}/storage")
    public ResponseEntity<LocationResponse> saveLocation(@Valid @RequestBody LocationRequest request,
                                                         @Parameter(description = "id of storage to be associate") @PathVariable UUID storageId) {
        log.debug("POST createLocation request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toLocation)
                .map(location -> locationService.save(location, storageId))
                .map(mapper::toLocationResponse)
                .map(locationResponse -> ResponseEntity.status(HttpStatus.CREATED).body(locationResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated location", content = @Content(schema = @Schema(implementation = LocationResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{locationId}")
    public ResponseEntity<LocationResponse> updateLocation(@Valid @RequestBody LocationRequest request,
                                                           @Parameter(description = "id of location to be updated")
                                                           @PathVariable("locationId") UUID locationId) {
        log.debug("PUT updateLocation request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toLocation)
                .map(location -> locationService.update(location, locationId))
                .map(mapper::toLocationResponse)
                .map(locationResponse -> ResponseEntity.status(HttpStatus.OK).body(locationResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Get all locations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Locations",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<LocationResponse>> getAll(SpecificationTemplate.LocationSpec spec,
                                                         @PageableDefault(page = 0, size = 10, sort = "locationId", direction = Sort.Direction.ASC)
                                                                 Pageable pageable, @RequestParam(required = false) UUID storageId) {
        List<LocationResponse> locationsList = new ArrayList<>();
        if (storageId != null) {
            locationsList = locationService.findAll(SpecificationTemplate.locationStorageId(storageId).and(spec))
                    .stream()
                    .map(mapper::toLocationResponse)
                    .sorted((o1, o2) -> o1.getDescription().
                            compareTo(o2.getDescription()))
                    .collect(Collectors.toList());
        } else {
            locationsList = locationService.findAll(spec)
                    .stream()
                    .map(mapper::toLocationResponse)
                    .sorted((o1, o2) -> o1.getDescription().
                            compareTo(o2.getDescription()))
                    .collect(Collectors.toList());
        }

        int start = (int) (pageable.getOffset() > locationsList.size() ? locationsList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > locationsList.size() ? locationsList.size()
                : (start + pageable.getPageSize()));
        Page<LocationResponse> locationsPageList = new PageImpl<>(locationsList.subList(start, end), pageable, locationsList.size());

        return ResponseEntity.status(HttpStatus.OK).body(locationsPageList);

    }

    @Operation(summary = "Get a location by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the location",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LocationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{locationId}")
    public ResponseEntity<LocationResponse> fetchOrFail(@Parameter(description = "id of location to be delete")
                                                        @PathVariable("locationId") UUID locationId) {
        return locationService.fetchOrFail(locationId)
                .map(mapper::toLocationResponse)
                .map(locationResponse -> ResponseEntity.status(HttpStatus.OK).body(locationResponse))
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete a location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted location!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Location not found",
                    content = @Content)})

    @DeleteMapping("/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable UUID locationId) {
        locationService.delete(locationId);
    }


}

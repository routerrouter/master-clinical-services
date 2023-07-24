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
import master.ao.storage.api.mapper.NatureMapper;
import master.ao.storage.api.request.NatureRequest;
import master.ao.storage.api.response.NatureResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.services.NatureService;
import master.ao.storage.core.domain.services.UtilService;
import master.ao.storage.core.domain.specifications.SpecificationTemplate;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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
@RequestMapping("/nature")
@Tag(name = "Nature", description = "The Nature API. Contains all operations that can be performed on a Nature")
public class NatureController {

    private final NatureService natureService;
    private final UtilService utilService;
    private final NatureMapper mapper;

    @Operation(summary = "Create nature")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nature created!",
                    content = @Content(schema = @Schema(implementation = NatureResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping()
    public ResponseEntity<NatureResponse> saveNature(@Valid @RequestBody NatureRequest request) {
        log.debug("POST createNature request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toNature)
                .map(nature -> natureService.save(nature))
                .map(mapper::toNatureResponse)
                .map(natureResponse -> ResponseEntity.status(HttpStatus.CREATED).body(natureResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update nature")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated nature", content = @Content(schema = @Schema(implementation = NatureResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Nature not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{natureId}")
    public ResponseEntity<NatureResponse> updateNature(@Valid @RequestBody NatureRequest request,
                                                       @Parameter(description = "id of nature to be updated")
                                                       @PathVariable("natureId") UUID natureId) {
        log.debug("PUT updateNature request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toNature)
                .map(nature -> natureService.update(nature, natureId))
                .map(mapper::toNatureResponse)
                .map(natureResponse -> ResponseEntity.status(HttpStatus.OK).body(natureResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all natures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Natures",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<Object>> getAll(@ParameterObject SpecificationTemplate.NatureSpec spec,
                                                       @ParameterObject @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        List<NatureResponse> naturesList = natureService.findAll(spec)
                .stream()
                .map(mapper::toNatureResponse)
                .sorted((o1, o2) -> o1.getName().
                        compareTo(o2.getName()))
                .collect(Collectors.toList());

        return utilService.getPageResponseEntity(pageable, new ArrayList<Object>(naturesList));

    }

    @Operation(summary = "Get a nature by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the nature",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NatureResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Nature not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{natureId}")
    public ResponseEntity<NatureResponse> fetchOrFail(@Parameter(description = "id of nature to be searched")
                                                      @PathVariable("natureId") UUID natureId) {
        return natureService.fetchOrFail(natureId)
                .map(mapper::toNatureResponse)
                .map(natureResponse -> ResponseEntity.status(HttpStatus.OK).body(natureResponse))
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete a nature")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted nature!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Nature not found",
                    content = @Content)})
    @DeleteMapping("/{natureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Parameter(description = "id of nature to be delete")
                       @PathVariable UUID natureId) {
        natureService.delete(natureId);
    }


}

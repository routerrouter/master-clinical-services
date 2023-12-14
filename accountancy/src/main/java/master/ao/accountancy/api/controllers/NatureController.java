package master.ao.accountancy.api.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.accountancy.api.mapper.NatureMapper;
import master.ao.accountancy.api.requests.NatureRequest;
import master.ao.accountancy.api.responses.AccountClassResponse;
import master.ao.accountancy.api.responses.NatureResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.NatureService;
import master.ao.accountancy.domain.services.UtilService;
import master.ao.accountancy.domain.specifications.SpecificationTemplate;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/natures")
@Tag(name = "Nature", description = "The Nature API. Contains all operations that can be performed on a Nature")
@RestController
public class NatureController {

    private final NatureService natureService;
    private final UtilService utilService;
    private final NatureMapper mapper;

    @Operation(summary = "Create Nature")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nature created!",
                    content = @Content(schema = @Schema(implementation = AccountClassResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping("/category/{categoryId}")
    private ResponseEntity<NatureResponse> createAccount(@Valid @RequestBody NatureRequest request,
                                                         @Parameter(description = "id of category associated") @PathVariable("categoryId") UUID categoryId) {
        log.debug("nature requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toAccountNature)
                .map(nature -> natureService.createAccountNature(nature, categoryId))
                .map(mapper::toNatureResponse)
                .map(natureResponse -> ResponseEntity.status(HttpStatus.CREATED).body(natureResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Update Nature")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated nature", content = @Content(schema = @Schema(implementation = NatureResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Nature not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{natureId}")
    public ResponseEntity<NatureResponse> updateNature(@Valid @RequestBody NatureRequest request,
                                                       @Parameter(description = "id of nature to be updated") @PathVariable("natureId") UUID natureId) {
        log.debug("PUT update Nature request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toAccountNature)
                .map(nature -> natureService.updateAccountNature(nature,natureId))
                .map(mapper::toNatureResponse)
                .map(natureResponse -> ResponseEntity.status(HttpStatus.OK).body(natureResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all natures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found natures",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<Object>> getAll(@ParameterObject SpecificationTemplate.NatureSpec spec,
                                               @Parameter(description = "id of category associated") @RequestParam(required = false) UUID categoryId,
                                               @ParameterObject @PageableDefault(page = 0, size = 10, sort = "description", direction = Sort.Direction.ASC) Pageable pageable) {
        List<NatureResponse> natureResponseList = natureService.findAll(spec, categoryId)
                .stream()
                .map(mapper::toNatureResponse)
                .sorted((o1, o2) -> o1.getDescription().
                        compareTo(o2.getDescription()))
                .collect(Collectors.toList());

        return utilService
                .getPageResponseEntity(pageable, new ArrayList<Object>(natureResponseList));

    }

    @Operation(summary = "Get a nature by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Nature",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NatureResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Nature class not found"),
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
            @ApiResponse(responseCode = "204", description = "Deleted Nature!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Nature class not found",
                    content = @Content)})
    @DeleteMapping("/{natureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Parameter(description = "id of Nature to be deleted") @PathVariable UUID natureId) {
        natureService.delete(natureId);
    }
}

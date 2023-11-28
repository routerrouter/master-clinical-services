package master.ao.accountancy.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import master.ao.accountancy.api.mapper.QuotaMapper;
import master.ao.accountancy.api.requests.QuotaRequest;
import master.ao.accountancy.api.responses.QuotaResponse;
import master.ao.accountancy.api.responses.QuotaResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.QuotaService;
import master.ao.accountancy.domain.services.UtilService;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/quotas")
@Tag(name = "Quota", description = "The Quota API. Contains all operations that can be performed on a Quota")
public class QuotaController {

    private final QuotaService quotaService;
    private final UtilService utilService;
    private final QuotaMapper mapper;

    @Operation(summary = "Create Quota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quota created!",
                    content = @Content(schema = @Schema(implementation = QuotaResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping
    private ResponseEntity<QuotaResponse> createQuota(@Valid @RequestBody QuotaRequest request) {
        log.debug("quota requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toQuota)
                .map(quota -> quotaService.saveQuota(quota))
                .map(mapper::toQuotaResponse)
                .map(quotaResponse -> ResponseEntity.status(HttpStatus.CREATED).body(quotaResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update Quota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quota updated!",
                    content = @Content(schema = @Schema(implementation = QuotaResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{quotaId}")
    private ResponseEntity<QuotaResponse> updateQuota(@Valid @RequestBody QuotaRequest request,
                                                        @Parameter(description = "id of quota to be updated") @PathVariable("quotaId") UUID quotaId ) {
        log.debug("PUT quota requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toQuota)
                .map(quota -> quotaService.updateQuota(quota,quotaId))
                .map(mapper::toQuotaResponse)
                .map(quotaResponse -> ResponseEntity.status(HttpStatus.CREATED).body(quotaResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all quotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Quotas",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/nature")
    public ResponseEntity<Page<Object>> getAll(@RequestParam(required = false) UUID natureId,
                                               @ParameterObject @PageableDefault(page = 0, size = 10, sort = "quotaId", direction = Sort.Direction.ASC) Pageable pageable) {
        List<QuotaResponse> quotaResponseList = quotaService.findAll(natureId)
                .stream()
                .map(mapper::toQuotaResponse)
                .collect(Collectors.toList());

        return utilService
                .getPageResponseEntity(pageable, new ArrayList<Object>(quotaResponseList));

    }


}

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
import master.ao.accountancy.api.mapper.CurrentMonthMapper;
import master.ao.accountancy.api.mapper.FinancialProgrammingMapper;
import master.ao.accountancy.api.requests.CurrentMonthRequest;
import master.ao.accountancy.api.requests.FinancialProgrammingRequest;
import master.ao.accountancy.api.responses.FinancialProgrammingResponse;
import master.ao.accountancy.api.responses.FinancialProgrammingResponse;
import master.ao.accountancy.api.responses.NatureResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.models.FinancialProgramming;
import master.ao.accountancy.domain.services.CurrentMonthService;
import master.ao.accountancy.domain.services.FinancialProgrammingService;
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

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/financial-programming")
@Tag(name = "Financial Programming", description = "The Financial Programming API. Contains all operations that can be performed on a Financial Programming")
@RestController
public class FinancialProgrammingController {

    private final FinancialProgrammingService programmingService;
    private final FinancialProgrammingMapper mapper;
    private final UtilService utilService;
    private final CurrentMonthService currentMonthService;

    @Operation(summary = "Create financial programming for Provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Financial Programming created!",
                    content = @Content(schema = @Schema(implementation = FinancialProgrammingResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void  createFinancialProgramming(@Valid @RequestBody List<FinancialProgrammingRequest> request) {
        log.debug("Financial Programming requested:{} ", request.toString());

        var programming = mapper.toFinancialProgrammingList(request);
        programmingService.create(programming);
    }


    @Operation(summary = "Edit a financial programming")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Financial Programming edited!",
                    content = @Content(schema = @Schema(implementation = FinancialProgrammingResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{programmingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void editFinancialProgramming(@Parameter(description = "id of financial programming to be updated") @RequestParam(required = false) UUID programmingId,
            @Valid @RequestBody FinancialProgrammingRequest request) {
        log.debug("Financial Programming requested:{} ", request.toString());

        var financialProgramming = mapper.toFinancialProgramming(request);
        programmingService.update(financialProgramming,programmingId);

    }

    @Operation(summary = "finalize programming")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Financial Programming finished!",
                    content = @Content(schema = @Schema(implementation = FinancialProgrammingResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void finallyFinancialProgramming() {
        var currentYear = currentMonthService.getActiveYear();
        log.debug("Financial Programming requestedFinalizing the financial programming for the period:{} ", currentYear.toString());
        programmingService.finallyProgramming();

    }


    @Operation(summary = "Get Financial Programming")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Financial Programmings",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<Object>> getAll(@Parameter(description = "id of nature to be searched") @RequestParam(required = false) UUID natureId,
                                               @ParameterObject @PageableDefault(page = 0, size = 10, sort = "programmingId", direction = Sort.Direction.ASC) Pageable pageable) {

        List<FinancialProgrammingResponse> natureResponseList = programmingService.findAllProgramming(natureId)
                .stream()
                .map(mapper::toFinancialProgrammingResponse)
                .collect(Collectors.toList());

        return utilService
                .getPageResponseEntity(pageable, new ArrayList<Object>(natureResponseList));

    }

    @Operation(summary = "Get a financial programming by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Programming",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NatureResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Programming not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{programmingId}")
    public ResponseEntity<FinancialProgrammingResponse> fetchOrFail(@Parameter(description = "id of programming to be searched")
                                                      @PathVariable("programmingId") UUID programmingId) {
        return programmingService.fetchOrFail(programmingId)
                .map(mapper::toFinancialProgrammingResponse)
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
                .orElse(ResponseEntity.notFound().build());

    }


    @Operation(summary = "Delete programming by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Programming deleted!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NatureResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Programming  not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @DeleteMapping("/{programmingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void  deleteProgramming(@Parameter(description = "id of programming to be deleted") @PathVariable("programmingId") UUID programmingId) {
        log.debug("Financial Programming to delete:{} ", programmingId.toString());
        programmingService.delete(programmingId);
    }



}

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
import master.ao.accountancy.api.mapper.BudgetMapper;
import master.ao.accountancy.api.requests.BudgetRequest;
import master.ao.accountancy.api.responses.BudgetResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.BudgetService;
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
@RequestMapping("/api/v1/budgets")
@Tag(name = "Budget", description = "The Budget API. Contains all operations that can be performed on a Budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final UtilService utilService;
    private final BudgetMapper mapper;

    @Operation(summary = "Save Budget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Budget saved!",
                    content = @Content(schema = @Schema(implementation = BudgetResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping
    private ResponseEntity<BudgetResponse> createBudget(@Valid @RequestBody BudgetRequest request) {
        log.debug("budget requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toBudget)
                .map(budget -> budgetService.saveBudget(budget))
                .map(mapper::toBudgetResponse)
                .map(budgetResponse -> ResponseEntity.status(HttpStatus.CREATED).body(budgetResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update Budget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budget updated!",
                    content = @Content(schema = @Schema(implementation = BudgetResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{budgetId}")
    private ResponseEntity<BudgetResponse> updateBudget(@Valid @RequestBody BudgetRequest request,
                                                        @Parameter(description = "id of budget to be updated") @PathVariable("budgetId") UUID budgetId ) {
        log.debug("PUT budget requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toBudget)
                .map(budget -> budgetService.updateBudget(budget,budgetId))
                .map(mapper::toBudgetResponse)
                .map(budgetResponse -> ResponseEntity.status(HttpStatus.CREATED).body(budgetResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all budgets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Budgets",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/nature")
    public ResponseEntity<Page<Object>> getAll(@RequestParam(required = false) UUID natureId,
                                               @ParameterObject @PageableDefault(page = 0, size = 10, sort = "budgetId", direction = Sort.Direction.ASC) Pageable pageable) {
        List<BudgetResponse> budgetResponseList = budgetService.findAll(natureId)
                .stream()
                .map(mapper::toBudgetResponse)
                .collect(Collectors.toList());

        return utilService
                .getPageResponseEntity(pageable, new ArrayList<Object>(budgetResponseList));

    }


}

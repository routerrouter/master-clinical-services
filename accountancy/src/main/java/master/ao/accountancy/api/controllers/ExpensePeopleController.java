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
import master.ao.accountancy.api.mapper.ExpensePeopleMapper;
import master.ao.accountancy.api.requests.ExpensePeopleRequest;
import master.ao.accountancy.api.responses.ExpensePeopleResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.ExpensePeopleService;
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
@RequestMapping("/api/v1/expenses")
@Tag(name = "ExpensePeople", description = "The ExpensePeople API. Contains all operations that can be performed on a ExpensePeople")
public class ExpensePeopleController {

    private final ExpensePeopleService expensePeopleService;
    private final UtilService utilService;
    private final ExpensePeopleMapper mapper;

    @Operation(summary = "Save ExpensePeople")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ExpensePeople saved!",
                    content = @Content(schema = @Schema(implementation = ExpensePeopleResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping
    private ResponseEntity<ExpensePeopleResponse> createExpensePeople(@Valid @RequestBody ExpensePeopleRequest request) {
        log.debug("expensePeople requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toExpensePeople)
                .map(expensePeople -> expensePeopleService.saveExpensePeople(expensePeople))
                .map(mapper::toExpensePeopleResponse)
                .map(expensePeopleResponse -> ResponseEntity.status(HttpStatus.CREATED).body(expensePeopleResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update ExpensePeople")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ExpensePeople updated!",
                    content = @Content(schema = @Schema(implementation = ExpensePeopleResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{expensePeopleId}")
    private ResponseEntity<ExpensePeopleResponse> updateExpensePeople(@Valid @RequestBody ExpensePeopleRequest request,
                                                        @Parameter(description = "id of expensePeople to be updated") @PathVariable("expensePeopleId") UUID expensePeopleId ) {
        log.debug("PUT expensePeople requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toExpensePeople)
                .map(expensePeople -> expensePeopleService.updateExpensePeople(expensePeople,expensePeopleId))
                .map(mapper::toExpensePeopleResponse)
                .map(expensePeopleResponse -> ResponseEntity.status(HttpStatus.CREATED).body(expensePeopleResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Expenses",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/nature")
    public ResponseEntity<Page<Object>> getAll(@RequestParam(required = false) UUID natureId,
                                               @Parameter(description = "id of nature to be searched") @ParameterObject @PageableDefault(page = 0, size = 10, sort = "expensePeopleId", direction = Sort.Direction.ASC) Pageable pageable) {
        List<ExpensePeopleResponse> expensePeopleResponseList = expensePeopleService.findAll(natureId)
                .stream()
                .map(mapper::toExpensePeopleResponse)
                .collect(Collectors.toList());

        return utilService
                .getPageResponseEntity(pageable, new ArrayList<Object>(expensePeopleResponseList));

    }


}

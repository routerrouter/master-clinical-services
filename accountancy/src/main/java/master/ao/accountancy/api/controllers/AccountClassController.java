package master.ao.accountancy.api.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.accountancy.api.mapper.AccountClassMapper;
import master.ao.accountancy.api.requests.AccountClassRequest;
import master.ao.accountancy.api.responses.AccountClassResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.AccountClassService;
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
@AllArgsConstructor
@RequestMapping("/api/v1/account-class")
@Tag(name = "Account class", description = "The Account class API. Contains all operations that can be performed on a Account class")
@RestController
public class AccountClassController {

    private final AccountClassService accountClassService;
    private final UtilService utilService;
    private final AccountClassMapper mapper;

    @Operation(summary = "Create Account class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account class created!",
                    content = @Content(schema = @Schema(implementation = AccountClassResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping()
    private ResponseEntity<AccountClassResponse> createAccountClass(@Valid @RequestBody AccountClassRequest request) {
        log.debug("account class requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toAccountClass)
                .map(accountClass -> accountClassService.createAccountClass(accountClass))
                .map(mapper::toAccountClassResponse)
                .map(accountClassResponse -> ResponseEntity.status(HttpStatus.CREATED).body(accountClassResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Update Account class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated accountClass", content = @Content(schema = @Schema(implementation = AccountClassResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Account class not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{accountClassId}")
    public ResponseEntity<AccountClassResponse> updateEntity(@Valid @RequestBody AccountClassRequest request,
                                                       @Parameter(description = "id of account class to be updated") @PathVariable("accountClassId") UUID accountClassId) {
        log.debug("PUT update Account class request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toAccountClass)
                .map(accountClass -> accountClassService.updateAccountClass(accountClassId,accountClass))
                .map(mapper::toAccountClassResponse)
                .map(accountClassResponse -> ResponseEntity.status(HttpStatus.OK).body(accountClassResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all accounts class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Accounts class",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<Object>> getAll(@ParameterObject SpecificationTemplate.AccountClassSpec spec,
                                               @ParameterObject @PageableDefault(page = 0, size = 10, sort = "number", direction = Sort.Direction.ASC) Pageable pageable) {
        List<AccountClassResponse> accountClassResponseList = accountClassService.findAllAccountClass(spec)
                .stream()
                .map(mapper::toAccountClassResponse)
                .sorted((o1, o2) -> o1.getNumber().
                        compareTo(o2.getNumber()))
                .collect(Collectors.toList());

        return utilService
                .getPageResponseEntity(pageable, new ArrayList<Object>(accountClassResponseList));

    }

    @Operation(summary = "Get a account class by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the account class",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccountClassResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account class not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{accountClassId}")
    public ResponseEntity<AccountClassResponse> fetchOrFail(@Parameter(description = "id of account class to be searched")
                                                      @PathVariable("accountClassId") UUID accountClassId) {
        return accountClassService.fetchOrFail(accountClassId)
                .map(mapper::toAccountClassResponse)
                .map(accountClassResponse -> ResponseEntity.status(HttpStatus.OK).body(accountClassResponse))
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete a account class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted account class!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account class not found",
                    content = @Content)})
    @DeleteMapping("/{accountClassId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Parameter(description = "id of account class to be deleted") @PathVariable UUID accountClassId) {
        accountClassService.delete(accountClassId);
    }
}

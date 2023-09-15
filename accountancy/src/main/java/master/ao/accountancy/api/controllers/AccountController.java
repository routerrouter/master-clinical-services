package master.ao.accountancy.api.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.accountancy.api.mapper.AccountMapper;
import master.ao.accountancy.api.requests.AccountRequest;
import master.ao.accountancy.api.responses.AccountClassResponse;
import master.ao.accountancy.api.responses.AccountResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.AccountService;
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
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts", description = "The Accounts API. Contains all operations that can be performed on a Account")
@RestController
public class AccountController {

    private final AccountService accountService;
    private final UtilService utilService;
    private final AccountMapper mapper;

    @Operation(summary = "Create Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created!",
                    content = @Content(schema = @Schema(implementation = AccountClassResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping("/accountClass/{classId}")
    private ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request,
                                                          @PathVariable("classId") UUID classId) {
        log.debug("account requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toAccount)
                .map(account -> accountService.createAccount(account, classId))
                .map(mapper::toAccountResponse)
                .map(accountResponse -> ResponseEntity.status(HttpStatus.CREATED).body(accountResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Update Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated account", content = @Content(schema = @Schema(implementation = AccountClassResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateEntity(@Valid @RequestBody AccountRequest request,
                                                       @Parameter(description = "id of account to be updated") @PathVariable("accountId") UUID accountId) {
        log.debug("PUT update Account request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toAccount)
                .map(account -> accountService.updateAccount(account,accountId))
                .map(mapper::toAccountResponse)
                .map(accountResponse -> ResponseEntity.status(HttpStatus.OK).body(accountResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Accounts",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<Object>> getAll(@ParameterObject SpecificationTemplate.AccountSpec spec,
                                               @ParameterObject @PageableDefault(page = 0, size = 10, sort = "number", direction = Sort.Direction.ASC) Pageable pageable) {
        List<AccountResponse> accountResponseList = accountService.findAll(spec)
                .stream()
                .map(mapper::toAccountResponse)
                .sorted((o1, o2) -> o1.getNumber().
                        compareTo(o2.getNumber()))
                .collect(Collectors.toList());

        return utilService
                .getPageResponseEntity(pageable, new ArrayList<Object>(accountResponseList));

    }

    @Operation(summary = "Get a account by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the account",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account class not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> fetchOrFail(@Parameter(description = "id of account to be searched")
                                                      @PathVariable("accountId") UUID accountId) {
        return accountService.fetchOrFail(accountId)
                .map(mapper::toAccountResponse)
                .map(accountResponse -> ResponseEntity.status(HttpStatus.OK).body(accountResponse))
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete a account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted account!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account class not found",
                    content = @Content)})
    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Parameter(description = "id of account to be deleted") @PathVariable UUID accountId) {
        accountService.delete(accountId);
    }
}

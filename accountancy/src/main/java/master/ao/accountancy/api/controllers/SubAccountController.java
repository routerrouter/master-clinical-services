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
import master.ao.accountancy.api.mapper.SubAccountMapper;
import master.ao.accountancy.api.requests.SubAccountRequest;
import master.ao.accountancy.api.responses.SubAccountResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.SubAccountService;
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
@RequestMapping("/api/v1/subaccounts")
@Tag(name = "SubAccounts", description = "The SubAccounts API. Contains all operations that can be performed on a SubAccount")
@RestController
public class SubAccountController {

    private final SubAccountService service;
    private final UtilService utilService;
    private final SubAccountMapper mapper;

    @Operation(summary = "Create SubAccount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SubAccount created!",
                    content = @Content(schema = @Schema(implementation = SubAccountResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Invalid id account"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping("/account/{accountId}")
    private ResponseEntity<SubAccountResponse> createSubAccount(@Valid @RequestBody SubAccountRequest request,
                                                                @Parameter(description = "id of Account associated") @PathVariable("accountId") UUID accountId) {
        log.debug("subAccount requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toSubAccount)
                .map(subAccount -> service.createSubAccount(subAccount, accountId))
                .map(mapper::toSubAccountResponse)
                .map(accountResponse -> ResponseEntity.status(HttpStatus.CREATED).body(accountResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Update SubAccount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated SubAccount", content = @Content(schema = @Schema(implementation = SubAccountResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "SubAccount not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{subAccountId}")
    public ResponseEntity<SubAccountResponse> updateEntity(@Valid @RequestBody SubAccountRequest request,
                                                       @Parameter(description = "id of SubAccount to be updated") @PathVariable("subAccountId") UUID subAccountId) {
        log.debug("PUT update SubAccount request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toSubAccount)
                .map(account -> service.updateSubAccount(account,subAccountId))
                .map(mapper::toSubAccountResponse)
                .map(accountResponse -> ResponseEntity.status(HttpStatus.OK).body(accountResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all subAccounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found SubAccounts",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<Object>> getAll(@ParameterObject SpecificationTemplate.SubAccountSpec spec,
                                               @ParameterObject @PageableDefault(page = 0, size = 10, sort = "number", direction = Sort.Direction.ASC) Pageable pageable,
                                               @RequestParam(required = false) UUID accountId) {

        List<SubAccountResponse> subAccountResponseList = service.findAll(spec, accountId)
                .stream()
                .map(mapper::toSubAccountResponse)
                .collect(Collectors.toList());

        return utilService
                .getPageResponseEntity(pageable, new ArrayList<Object>(subAccountResponseList));

    }

    @Operation(summary = "Get a subAccount by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the subAccount",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SubAccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "SubAccount class not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{subAccountId}")
    public ResponseEntity<SubAccountResponse> fetchOrFail(@Parameter(description = "id of subAccount to be searched") @PathVariable("subAccountId") UUID subAccountId) {
        return service.fetchOrFail(subAccountId)
                .map(mapper::toSubAccountResponse)
                .map(subAccountResponse -> ResponseEntity.status(HttpStatus.OK).body(subAccountResponse))
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete a subAccount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted subAccount!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "SubAccount class not found",
                    content = @Content)})
    @DeleteMapping("/{subAccountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Parameter(description = "id of subAccount to be deleted") @PathVariable UUID subAccountId) {
        service.delete(subAccountId);
    }
}

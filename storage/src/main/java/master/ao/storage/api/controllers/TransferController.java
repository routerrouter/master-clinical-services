package master.ao.storage.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.config.security.UserDetailsImpl;
import master.ao.storage.api.mapper.TransferMapper;
import master.ao.storage.api.request.TransferRequest;
import master.ao.storage.api.response.TransferResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.services.TransferService;
import master.ao.storage.core.domain.services.UtilService;
import master.ao.storage.core.domain.specifications.SpecificationTemplate;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
@Tag(name = "Transfer", description = "The Transfer API. Contains all operations that can be performed on transfer")
public class TransferController {

    private final TransferService transferService;
    private final UtilService utilService;
    private final TransferMapper mapper;


    @Operation(summary = "Save transfer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transfer saved", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping
    public ResponseEntity<TransferResponse> saveTransfer(@Valid @RequestBody TransferRequest request,
                                                         Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("POST createStorage request received {} ", request.toString());

        return Stream.of(request)
                .map(mapper::toTransfer)
                .map(transfer -> transferService.saveTransfer(transfer, userDetails.getUserId()))
                .map(mapper::toTransferResponse)
                .map(transferResponse -> ResponseEntity.status(HttpStatus.CREATED).body(transferResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all Transfers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Locations",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/{storageId}/storage")
    public ResponseEntity<Page<Object>> getAll(@ParameterObject SpecificationTemplate.LocationSpec spec,
                                                         @ParameterObject  @PageableDefault(page = 0, size = 10, sort = "transfer_Date", direction = Sort.Direction.ASC)
                                                                 Pageable pageable, @PathVariable UUID storageId) {
        List<TransferResponse> transferResponseList = new ArrayList<>();
        transferResponseList = transferService.listByStorage(storageId)
                .stream()
                .map(mapper::toTransferResponse)
                .sorted(Comparator.comparing(TransferResponse::getTransferDate))
                .collect(Collectors.toList());

        return utilService.getPageResponseEntity(pageable, new ArrayList<Object>(transferResponseList));

    }

}

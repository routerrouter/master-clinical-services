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
import master.ao.storage.api.response.LocationResponse;
import master.ao.storage.api.response.TransferResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.models.Transfer;
import master.ao.storage.core.domain.services.TransferService;
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
    private final TransferMapper mapper;


    @Operation(summary = "Save transfer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transfer saved", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping
    public ResponseEntity<TransferResponse> saveTransfer(@Valid @RequestBody TransferRequest request,
                                                         Authentication authentication,
                                                         @RequestParam  UUID originStorageId) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("POST createStorage request received {} ", request.toString());

        return Stream.of(request)
                .map(mapper::toTransfer)
                .map(transfer -> transferService.saveTransfer(transfer, originStorageId, userDetails.getUserId()))
                .map(mapper::toTransferResponse)
                .map(transferResponse -> ResponseEntity.status(HttpStatus.CREATED).body(transferResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Get all locations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Locations",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<TransferResponse>> getAll(@ParameterObject SpecificationTemplate.LocationSpec spec,
                                                         @ParameterObject  @PageableDefault(page = 0, size = 10, sort = "locationId", direction = Sort.Direction.ASC)
                                                                 Pageable pageable, @RequestParam(required = false) UUID storageId) {
        List<TransferResponse> transferResponseList = new ArrayList<>();
        transferResponseList = transferService.listAll()
                .stream()
                .map(mapper::toTransferResponse)
                .sorted((transfer1, transfer2) -> transfer1.getTransferDate().
                        compareTo(transfer2.getTransferDate()))
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > transferResponseList.size() ? transferResponseList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > transferResponseList.size() ? transferResponseList.size()
                : (start + pageable.getPageSize()));
        Page<TransferResponse> transferResponsePage = new PageImpl<>(transferResponseList.subList(start, end), pageable, transferResponseList.size());
        return ResponseEntity.status(HttpStatus.OK).body(transferResponsePage);

    }









    /*private ResponseEntity<Page<TransferResponse>> getPageResponseEntity(@ParameterObject @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                                                                         List<TransferResponse> transferResponseList) {
        int start = (int) (pageable.getOffset() > transferResponseList.size() ? transferResponseList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > transferResponseList.size() ? transferResponseList.size()
                : (start + pageable.getPageSize()));
        Page<TransferResponse> productTransferPageList = new PageImpl<>(transferResponseList.subList(start, end), pageable, transferResponseList.size());

        return ResponseEntity.status(HttpStatus.OK).body(productTransferPageList);
    }*/

}

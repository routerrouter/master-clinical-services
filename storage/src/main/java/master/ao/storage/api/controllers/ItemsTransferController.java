package master.ao.storage.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.mapper.ItemsItemsMovementMapper;
import master.ao.storage.api.mapper.ItemsTransferMapper;
import master.ao.storage.api.response.ItemsMovementResponse;
import master.ao.storage.api.response.ItemsTransferResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.services.MovementService;
import master.ao.storage.core.domain.services.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/itemsTransfer")
@RequiredArgsConstructor
@Tag(name = "itemsTransfer", description = "The itemsTransfer API. Contains all operations that can be performed on a itemsTransfer")
public class ItemsTransferController {

    private final TransferService transferService;
    private final ItemsTransferMapper mapper;


    @Operation(summary = "Get all items by transfer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found items transfer",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid transfer id supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/{transferId}/transfer")
    public ResponseEntity<List<ItemsTransferResponse>> listAllItemsMovement(@Parameter(description = "id of transfer to be find") @PathVariable UUID transferId) {

        log.debug("GET transfer items list");

        List<ItemsTransferResponse>  itemsTransferResponseList = transferService.listItemsByTransfer(transferId)
                .stream()
                .map(mapper::toItemsTransferResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemsTransferResponseList);
    }

}

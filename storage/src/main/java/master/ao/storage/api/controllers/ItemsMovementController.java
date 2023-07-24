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
import master.ao.storage.api.response.ItemsMovementResponse;
import master.ao.storage.api.response.ProductResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.services.MovementService;
import master.ao.storage.core.domain.services.UtilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/itemsMovement")
@RequiredArgsConstructor
@Tag(name = "ItemsMovement", description = "The ItemsMovement API. Contains all operations that can be performed on a ItemsMovement")
public class ItemsMovementController {

    private final MovementService movementService;
    private final UtilService utilService;
    private final ItemsItemsMovementMapper itemsMovementMapper;

    @Operation(summary = "Get all items movement by its movement id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{movementId}/movement")
    public ResponseEntity<List<ItemsMovementResponse>> listAllItemsMovement(@Parameter(description = "id of movement to be updated") @PathVariable UUID movementId) {

        log.debug("GET movement items list");

        List<ItemsMovementResponse> itemsMovementResponseList = movementService
                .listItemsByMovement(movementId)
                .stream()
                .map(itemsMovementMapper::toItemsMovementResponse)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(itemsMovementResponseList);
    }


}

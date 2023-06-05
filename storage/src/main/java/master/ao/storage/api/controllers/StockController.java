package master.ao.storage.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import master.ao.storage.api.mapper.StockMapper;
import master.ao.storage.api.request.StockRequest;
import master.ao.storage.api.response.ProductResponse;
import master.ao.storage.api.response.StockResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.services.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
@Tag(name = "Stock", description = "The Stock API. Contains all operations that can be performed on stock product")
public class StockController {

    private final StockService stockService;
    private final StockMapper mapper;

    @Operation(summary = "Inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory found ", content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Storage not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/{storageId}/storage")
    public ResponseEntity<List<StockResponse>> getInventory(@Parameter(description = "id of storage to be searched") @PathVariable UUID storageId) {
        var inventory = stockService.findInventory(storageId)
                .stream()
                .map(mapper::toStockResponse)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(inventory);
    }

    @Operation(summary = "Get all critical product in stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Critical stock items found ", content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Storage not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/critical/{storageId}/storage")
    public ResponseEntity<List<StockResponse>> getAllCritical(@Parameter(description = "id of storage to be searched") @PathVariable UUID storageId) {
        var criticalProductsList = stockService.findCriticalProducts(storageId)
                .stream()
                .map(mapper::toStockResponse)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(criticalProductsList);
    }

    @Operation(summary = "Get all expired product in stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expired stock items found ", content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Storage not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/expired/{storageId}/storage")
    public ResponseEntity<List<StockResponse>> getAllExpired(@Parameter(description = "id of storage to be searched") @PathVariable UUID storageId) {
        var expiredProductsList = stockService.findExpiredProducts(storageId)
                .stream()
                .map(mapper::toStockResponse)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(expiredProductsList);
    }

    @Operation(summary = "Update product cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated product cost", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Group not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProductCost(@Valid @RequestBody StockRequest request) {
        var stock = mapper.toStock(request);
        stockService.updateProductCost(stock);
    }

}

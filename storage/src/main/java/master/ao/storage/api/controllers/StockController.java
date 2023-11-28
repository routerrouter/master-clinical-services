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
import master.ao.storage.api.request.StockPutRequest;
import master.ao.storage.api.response.StockResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.models.Stock;
import master.ao.storage.core.domain.services.StockService;
import master.ao.storage.core.domain.services.UtilService;
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
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
@Tag(name = "Stock", description = "The Stock API. Contains all operations that can be performed on stock product")
public class StockController {

    private final StockService stockService;
    private final UtilService utilService;
    private final StockMapper mapper;

    @Operation(summary = "Inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory found ", content = @Content(schema = @Schema(implementation = StockResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Storage not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/{storageId}/storage")
    public ResponseEntity<Page<Object>> getInventory(@Parameter(description = "id of storage to be searched")
                                                            @PathVariable UUID storageId,
                                                            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        var inventoryList = stockService.findInventory(storageId)
                .stream()
                .map(mapper::toStockResponse)
                .collect(Collectors.toList());

        return utilService.getPageResponseEntity(pageable, new ArrayList<Object>(inventoryList));
    }

    @Operation(summary = "Get all critical product in stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Critical stock items found ", content = @Content(schema = @Schema(implementation = StockResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Storage not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/critical/{storageId}/storage")
    public ResponseEntity<Page<Object>> getAllCritical(@Parameter(description = "id of storage to be searched") @PathVariable UUID storageId,
                                                              @ParameterObject @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        var criticalProductsList = stockService.findCriticalProducts(storageId)
                .stream()
                .map(mapper::toStockResponse)
                .collect(Collectors.toList());

        return utilService.getPageResponseEntity(pageable, new ArrayList<Object>(criticalProductsList));
    }

    @Operation(summary = "Get all expired product in stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expired stock items found ", content = @Content(schema = @Schema(implementation = StockResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Storage not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/expired/{storageId}/storage")
    public ResponseEntity<Page<Object>> getAllExpired(@Parameter(description = "id of storage to be searched") @PathVariable UUID storageId,
                                                             @ParameterObject @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        var expiredProductsList = stockService.findExpiredProducts(storageId)
                .stream()
                .map(mapper::toStockResponse)
                .collect(Collectors.toList());

        return utilService.getPageResponseEntity(pageable, new ArrayList<Object>(expiredProductsList));
    }

    @Operation(summary = "Get all product stock by location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products stock items found ", content = @Content(schema = @Schema(implementation = StockResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/{locationId}/location")
    public ResponseEntity<Page<Object>> getAllProductStockInLocation(@Parameter(description = "id of location to be searched") @PathVariable UUID locationId,
                                                             @ParameterObject @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        var inLocationProductsList = stockService.findByLocation(locationId)
                .stream()
                .map(mapper::toStockResponse)
                .collect(Collectors.toList());

        return utilService.getPageResponseEntity(pageable, new ArrayList<Object>(inLocationProductsList));
    }

    @Operation(summary = "Get product existence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products stock items found ", content = @Content(schema = @Schema(implementation = StockResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id product"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/{productId}/product")
    public ResponseEntity<Page<Object>> getExistenceByProductId(@Parameter(description = "id of product to be searched") @PathVariable UUID productId,
                                                                            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        var inLocationProductsList = stockService.findExistenceByProduct(productId)
                .stream()
                .map(mapper::toStockResponse)
                .collect(Collectors.toList());

        return utilService.getPageResponseEntity(pageable, new ArrayList<Object>(inLocationProductsList));
    }

    @Operation(summary = "Get product existence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products stock items found ", content = @Content(schema = @Schema(implementation = StockResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id product"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/existence/product/{productId}")
    public Long getExistence(
            @RequestParam(value = "lote", required = false) String lote,
            @RequestParam(value = "expirated", required = false) String expirationDate,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "lifespan", required = false) Integer lifespan,
            @RequestParam(value = "storageId", required = false) UUID storageId,
            @PathVariable UUID productId) {

        return stockService.fetchExistenceByProduct(productId, storageId, lote, expirationDate, model, lifespan);
    }

    @Operation(summary = "Update product existence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated product existence", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Group not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})

    @PatchMapping("/updateQuantity")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProductQuantity(@Valid @RequestBody  StockPutRequest request) {
        var stock = mapper.toStock(request);
        stockService.updateProductExistence(stock);
    }

    @Operation(summary = "Update product cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated product cost", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Group not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})

    @PatchMapping("/updateCost")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProductCost(@Valid @RequestBody StockPutRequest request) {
        var stock = mapper.toStock(request);
        stockService.updateProductCost(stock);
    }


}

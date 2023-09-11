package master.ao.storage.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.enums.UpdateStockType;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.models.LogStock;
import master.ao.storage.core.domain.services.LogStockService;
import master.ao.storage.core.domain.services.UtilService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;


@RestController
@RequiredArgsConstructor
@RequestMapping("/stock-log")
@Tag(name = "Stock", description = "The LogStock API. Contains all operations that can be performed on update stock")
public class LogStockController {

    private final LogStockService logStockService;
    private final UtilService utilService;

    @Operation(summary = "LogStock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LogStock found ", content = @Content(schema = @Schema(implementation = LogStock.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid update type"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping()
    public ResponseEntity<Page<Object>> getInventory(@Parameter(description = "id of storage to be searched")
                                                     @RequestParam(required = false) UpdateStockType type,
                                                     @RequestParam(required = false) String initialDate,
                                                     @RequestParam(required = false) String finalDate,
                                                     @ParameterObject @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {


        LocalDate initial = LocalDate.parse(initialDate);
        LocalDate endDate = LocalDate.parse(finalDate);
        var logUpdateStockList = logStockService.findAll(type,initial,endDate);

        return utilService.getPageResponseEntity(pageable, new ArrayList<Object>(logUpdateStockList));
    }


}

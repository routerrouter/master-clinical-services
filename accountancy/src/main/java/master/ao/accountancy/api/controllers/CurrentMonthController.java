package master.ao.accountancy.api.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.accountancy.api.mapper.CurrentMonthMapper;
import master.ao.accountancy.api.requests.CurrentMonthRequest;
import master.ao.accountancy.api.responses.CurrentMonthResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.CurrentMonthService;
import master.ao.accountancy.domain.services.UtilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/current-year")
@Tag(name = "Current month", description = "The Current month API. Contains all operations that can be performed on a Current month")
@RestController
public class CurrentMonthController {

    private final CurrentMonthService currentMonthService;
    private final CurrentMonthMapper mapper;

    @Operation(summary = "Create CurrentMonth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CurrentMonth created!",
                    content = @Content(schema = @Schema(implementation = CurrentMonthResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void createCurrentMonth(@Valid @RequestBody CurrentMonthRequest request) {
        log.debug("CurrentMonth requested:{} ", request.toString());

        var currentMonth = mapper.toCurrentMonth(request);
        currentMonthService.activeCurrentYear(currentMonth);
    }


    @Operation(summary = "Get current month")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found CurrentMonths",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<?> getAll() {

        return ResponseEntity.status(HttpStatus.OK).body(currentMonthService.getActiveYear());

    }

}

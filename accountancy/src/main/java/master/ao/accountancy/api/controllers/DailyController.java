package master.ao.accountancy.api.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import master.ao.accountancy.api.mapper.DailyMapper;
import master.ao.accountancy.api.requests.DailyRequest;
import master.ao.accountancy.api.responses.DailyResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.DailyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dailies")
@Tag(name = "Daily", description = "The Daily API. Contains all operations that can be performed on a Daily")
public class DailyController {

    private final DailyService dailyService;
    private final DailyMapper mapper;

    @Operation(summary = "Save Daily")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Daily saved!",
                    content = @Content(schema = @Schema(implementation = DailyResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping
    private ResponseEntity<DailyResponse> createDaily(@Valid @RequestBody DailyRequest request) {
        log.debug("daily requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toDaily)
                .map(daily -> dailyService.save(daily))
                .map(mapper::toDailyResponse)
                .map(dailyResponse -> ResponseEntity.status(HttpStatus.CREATED).body(dailyResponse))
                .findFirst()
                .get();
    }
}

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
import master.ao.storage.api.mapper.EntityMapper;
import master.ao.storage.api.mapper.ItemsItemsMovementMapper;
import master.ao.storage.api.mapper.MovementMapper;
import master.ao.storage.api.request.MovementRequest;
import master.ao.storage.api.response.*;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.models.Entities;
import master.ao.storage.core.domain.services.MovementService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/movements")
@Tag(name = "Movement", description = "The Movement API. Contains all operations that can be performed on a movement. INPUT|ALL OUTPUTS|BUY|ORDER|TRANSFER|REQUEST")
public class MovementController {

    private final MovementService movementService;
    private final UtilService utilService;
    private final MovementMapper mapper;
    private final EntityMapper entityMapper;


    @PostMapping()
    public ResponseEntity<MovementResponse> saveMovement(@Valid @RequestBody MovementRequest request) {
        log.debug("POST saveMovement request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toMovement)
                .map(movement -> movementService.save(movement))
                .map(mapper::toMovementResponse)
                .map(groupResponse -> ResponseEntity
                        .status(HttpStatus.CREATED).body(groupResponse))
                .findFirst()
                .get();
    }

    @GetMapping()
    public ResponseEntity<Page<Object>> listAllMovements(@ParameterObject SpecificationTemplate.MovementSpec spec,
                                                                   @ParameterObject @PageableDefault(page = 0, size = 10, sort = "movementDate", direction = Sort.Direction.ASC) Pageable pageable,
                                                                   @RequestParam(required = false) String initialDate,
                                                                   @RequestParam(required = false) String finalDate,
                                                                   @RequestParam(required = false) UUID entityId) {

        log.debug("GET list all movements");

        List<MovementResponse> movementResponseList = new ArrayList<>();
        LocalDate initial = LocalDate.parse(initialDate);
        LocalDate endDate = LocalDate.parse(finalDate);


        if (entityId != null) {
            movementResponseList = movementService.listAndFilterAllMovements(SpecificationTemplate.movementEntityId(entityId)
                    .and(spec),initial, endDate)
                    .stream()
                    .map(mapper::toMovementResponse)
                    .collect(Collectors.toList());
        } else {
            movementResponseList = movementService.listAndFilterAllMovements(spec, initial, endDate)
                    .stream()
                    .map(mapper::toMovementResponse)
                    .collect(Collectors.toList());
        }


        return utilService.getPageResponseEntity(pageable, new ArrayList<Object>(movementResponseList));
    }

    @Operation(summary = "Get all entities with pending rerquest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Entities",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/movement-request/entities")
    public ResponseEntity<List<EntityShortResponse>> getAllWithPendingRequest() {
        List<EntityShortResponse> entityList = movementService.listEntitiesWithPendingRequest()
                .stream()
                .map(entityMapper::toEntityShortResponse)
                .sorted((o1, o2) -> o1.getName().
                        compareTo(o2.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(entityList);

    }

    @PatchMapping("/movement-request/status/{movementId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyMovementStatus(@PathVariable UUID movementId) {
        movementService.updateRequestMovementStatus(movementId);
    }





}

package master.ao.storage.api.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.config.security.UserDetailsImpl;
import master.ao.storage.api.mapper.MovementMapper;
import master.ao.storage.api.request.MovementRequest;
import master.ao.storage.api.response.MovementResponse;
import master.ao.storage.core.domain.services.MovementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/movements")
@Tag(name = "Movement", description = "The Movement API. Contains all operations that can be performed on a movement")
public class MovementController {

    private final MovementService movementService;
    private final MovementMapper mapper;


    @PostMapping()
    public ResponseEntity<MovementResponse> saveGroup(@Valid @RequestBody MovementRequest request,
                                                      Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("POST createGroup request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toMovement)
                .map(movement -> movementService.save(movement, userDetails.getUserId()))
                .map(mapper::toMovementResponse)
                .map(groupResponse -> ResponseEntity.status(HttpStatus.CREATED).body(groupResponse))
                .findFirst()
                .get();
    }



}

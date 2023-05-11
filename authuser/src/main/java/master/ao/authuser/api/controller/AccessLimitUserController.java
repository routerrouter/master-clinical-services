package master.ao.authuser.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.authuser.api.mapper.AccessLimitUserMapper;
import master.ao.authuser.api.request.AccessLimitRequest;
import master.ao.authuser.api.response.AccessLimitResponse;
import master.ao.authuser.core.domain.service.AccessLimitUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/accessLimit")
public class AccessLimitUserController {

    private final AccessLimitUserMapper mapper;
    private final AccessLimitUserService service;

    @PostMapping("/{userId}/user")
    public ResponseEntity<AccessLimitResponse> saveAccessLimit(@Valid @RequestBody AccessLimitRequest request,
                                                               @PathVariable(value = "userId") UUID userId){
        log.debug("POST saveAccessLimit request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toAcessLimitUser)
                .map(limit -> service.save(limit,userId))
                .map(mapper::toAccessLimitResponse)
                .map(accessLimitUserResponse -> ResponseEntity.status(HttpStatus.CREATED).body(accessLimitUserResponse))
                .findFirst()
                .get();
    }

    @PutMapping("/{userId}/user")
    public ResponseEntity<AccessLimitResponse> updateAccessLimit(@PathVariable(value = "userId") UUID userId,
                                                                 @Valid @RequestBody AccessLimitRequest request){
        log.debug("PUT updateAccessLimit AccessLimitRequest received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toAcessLimitUser)
                .map(limitUser -> service.update(limitUser, userId))
                .map(mapper::toAccessLimitResponse)
                .map(accessLimitUserResponse -> ResponseEntity.status(HttpStatus.OK).body(accessLimitUserResponse))
                .findFirst()
                .get();
    }

    @GetMapping()
    public ResponseEntity<Page<AccessLimitResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "ativation", direction = Sort.Direction.DESC) Pageable pageable) {

        List<AccessLimitResponse> accessLimitResponses = service.findAll()
                    .stream()
                    .map(mapper::toAccessLimitResponse)
                    .collect(Collectors.toList());


        int start = (int) (pageable.getOffset() > accessLimitResponses.size() ? accessLimitResponses.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > accessLimitResponses.size() ? accessLimitResponses.size()
                : (start + pageable.getPageSize()));
        Page<AccessLimitResponse> responsePage = new PageImpl<>(accessLimitResponses.subList(start, end), pageable, accessLimitResponses.size());

        return ResponseEntity.status(HttpStatus.OK).body(responsePage);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<AccessLimitResponse> findByUserId(@PathVariable(value = "userId") UUID userId){
        return service.findByUserId(userId)
                .map(mapper::toAccessLimitResponse)
                .map(accessLimitUserResponse -> ResponseEntity.status(HttpStatus.OK).body(accessLimitUserResponse))
                .orElse(ResponseEntity.notFound().build());
    }

}

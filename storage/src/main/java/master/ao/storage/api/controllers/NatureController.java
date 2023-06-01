package master.ao.storage.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.config.security.UserDetailsImpl;
import master.ao.storage.api.mapper.NatureMapper;
import master.ao.storage.api.request.NatureRequest;
import master.ao.storage.api.response.NatureResponse;
import master.ao.storage.core.domain.services.NatureService;
import master.ao.storage.core.domain.specifications.SpecificationTemplate;
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
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/nature")

public class NatureController {

    private final NatureService natureService;
    private final NatureMapper mapper;


    @PostMapping()
    public ResponseEntity<NatureResponse> saveNature(@Valid @RequestBody NatureRequest request,
                                                     Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("POST createNature request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toNature)
                .map(nature -> natureService.save(nature, userDetails.getUserId()))
                .map(mapper::toNatureResponse)
                .map(natureResponse -> ResponseEntity.status(HttpStatus.CREATED).body(natureResponse))
                .findFirst()
                .get();
    }


    @PutMapping("/{natureId}")
    public ResponseEntity<NatureResponse> updateNature(@Valid @RequestBody NatureRequest request,
                                                       @PathVariable("natureId") UUID natureId) {
        log.debug("PUT updateNature request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toNature)
                .map(nature -> natureService.update(nature, natureId))
                .map(mapper::toNatureResponse)
                .map(natureResponse -> ResponseEntity.status(HttpStatus.OK).body(natureResponse))
                .findFirst()
                .get();
    }


    @GetMapping
    public ResponseEntity<Page<NatureResponse>> getAll(SpecificationTemplate.NatureSpec spec,
                                                       @PageableDefault(page = 0, size = 10, sort = "natureId", direction = Sort.Direction.ASC) Pageable pageable) {
        var naturesList = natureService.findAll(spec)
                .stream()
                .map(mapper::toNatureResponse)
                .sorted((o1, o2) -> o1.getName().
                        compareTo(o2.getName()))
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > naturesList.size() ? naturesList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > naturesList.size() ? naturesList.size()
                : (start + pageable.getPageSize()));
        Page<NatureResponse> naturesPageList = new PageImpl<>(naturesList.subList(start, end), pageable, naturesList.size());

        return ResponseEntity.status(HttpStatus.OK).body(naturesPageList);

    }


    @GetMapping("/{natureId}")
    public ResponseEntity<NatureResponse> fetchOrFail(@PathVariable("natureId") UUID natureId) {
        return natureService.fetchOrFail(natureId)
                .map(mapper::toNatureResponse)
                .map(natureResponse -> ResponseEntity.status(HttpStatus.OK).body(natureResponse))
                .orElse(ResponseEntity.notFound().build());

    }


    @DeleteMapping("/{natureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable UUID natureId) {
        natureService.delete(natureId);
    }


}

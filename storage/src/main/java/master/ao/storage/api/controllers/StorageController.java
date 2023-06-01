package master.ao.storage.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.config.security.UserDetailsImpl;
import master.ao.storage.api.mapper.StorageMapper;
import master.ao.storage.api.request.StorageRequest;
import master.ao.storage.api.response.StorageResponse;
import master.ao.storage.core.domain.services.StorageService;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/storage")

public class StorageController {

    private final StorageService storageService;
    private final StorageMapper mapper;


    @PostMapping()
    public ResponseEntity<StorageResponse> saveStorage(@Valid @RequestBody StorageRequest request,
                                                       Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("POST createStorage request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toStorage)
                .map(storage -> storageService.save(storage, userDetails.getUserId()))
                .map(mapper::toStorageResponse)
                .map(StorageResponse -> ResponseEntity.status(HttpStatus.CREATED).body(StorageResponse))
                .findFirst()
                .get();
    }


    @PutMapping("/{storageId}")
    public ResponseEntity<StorageResponse> updateStorage(@Valid @RequestBody StorageRequest request,
                                                         @PathVariable("storageId") UUID storageId) {
        log.debug("PUT updateStorage request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toStorage)
                .map(storage -> storageService.update(storage, storageId))
                .map(mapper::toStorageResponse)
                .map(StorageResponse -> ResponseEntity.status(HttpStatus.OK).body(StorageResponse))
                .findFirst()
                .get();
    }


    @GetMapping("/forLogin")
    public ResponseEntity<List<StorageResponse>> getListStorage(SpecificationTemplate.StorageSpec spec) {
        var storagesList = storageService.findAll(spec)
                .stream()
                .map(mapper::toStorageResponse)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(storagesList);

    }

    @GetMapping
    public ResponseEntity<Page<StorageResponse>> getAll(SpecificationTemplate.StorageSpec spec,
                                                        @PageableDefault(page = 0, size = 10, sort = "StorageId", direction = Sort.Direction.ASC) Pageable pageable) {
        var storagesList = storageService.findAll(spec)
                .stream()
                .map(mapper::toStorageResponse)
                .sorted((o1, o2) -> o1.getName().
                        compareTo(o2.getName()))
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > storagesList.size() ? storagesList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > storagesList.size() ? storagesList.size()
                : (start + pageable.getPageSize()));
        Page<StorageResponse> storagesPageList = new PageImpl<>(storagesList.subList(start, end), pageable, storagesList.size());

        return ResponseEntity.status(HttpStatus.OK).body(storagesPageList);

    }


    @GetMapping("/{storageId}")
    public ResponseEntity<StorageResponse> fetchOrFail(@PathVariable("storageId") UUID storageId) {
        return storageService.fetchOrFail(storageId)
                .map(mapper::toStorageResponse)
                .map(storageResponse -> ResponseEntity.status(HttpStatus.OK).body(storageResponse))
                .orElse(ResponseEntity.notFound().build());

    }


    @DeleteMapping("/{storageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable UUID storageId) {
        storageService.delete(storageId);
    }


}

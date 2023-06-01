package master.ao.storage.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.mapper.LocationMapper;
import master.ao.storage.api.request.LocationRequest;
import master.ao.storage.api.response.LocationResponse;
import master.ao.storage.core.domain.services.LocationService;
import master.ao.storage.core.domain.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/location")

public class LocationController {

    private final LocationService locationService;
    private final LocationMapper mapper;


    @PostMapping("/{storageId}/storage")
    public ResponseEntity<LocationResponse> saveLocation(@Valid @RequestBody LocationRequest request,
                                                         @PathVariable UUID storageId) {
        log.debug("POST createLocation request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toLocation)
                .map(location -> locationService.save(location, storageId))
                .map(mapper::toLocationResponse)
                .map(locationResponse -> ResponseEntity.status(HttpStatus.CREATED).body(locationResponse))
                .findFirst()
                .get();
    }


    @PutMapping("/{locationId}")
    public ResponseEntity<LocationResponse> updateLocation(@Valid @RequestBody LocationRequest request,
                                                           @PathVariable("locationId") UUID locationId) {
        log.debug("PUT updateLocation request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toLocation)
                .map(location -> locationService.update(location, locationId))
                .map(mapper::toLocationResponse)
                .map(locationResponse -> ResponseEntity.status(HttpStatus.OK).body(locationResponse))
                .findFirst()
                .get();
    }


    @GetMapping
    public ResponseEntity<Page<LocationResponse>> getAll(SpecificationTemplate.LocationSpec spec,
                                                         @PageableDefault(page = 0, size = 10, sort = "locationId", direction = Sort.Direction.ASC)
                                                                 Pageable pageable, @RequestParam(required = false) UUID storageId) {
        List<LocationResponse> locationsList = new ArrayList<>();
        if (storageId != null) {
            locationsList = locationService.findAll(SpecificationTemplate.locationStorageId(storageId).and(spec))
                    .stream()
                    .map(mapper::toLocationResponse)
                    .sorted((o1, o2) -> o1.getDescription().
                            compareTo(o2.getDescription()))
                    .collect(Collectors.toList());
        } else {
            locationsList = locationService.findAll(spec)
                    .stream()
                    .map(mapper::toLocationResponse)
                    .sorted((o1, o2) -> o1.getDescription().
                            compareTo(o2.getDescription()))
                    .collect(Collectors.toList());
        }

        int start = (int) (pageable.getOffset() > locationsList.size() ? locationsList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > locationsList.size() ? locationsList.size()
                : (start + pageable.getPageSize()));
        Page<LocationResponse> locationsPageList = new PageImpl<>(locationsList.subList(start, end), pageable, locationsList.size());

        return ResponseEntity.status(HttpStatus.OK).body(locationsPageList);

    }


    @GetMapping("/{locationId}")
    public ResponseEntity<LocationResponse> fetchOrFail(@PathVariable("locationId") UUID locationId) {
        return locationService.fetchOrFail(locationId)
                .map(mapper::toLocationResponse)
                .map(locationResponse -> ResponseEntity.status(HttpStatus.OK).body(locationResponse))
                .orElse(ResponseEntity.notFound().build());

    }


    @DeleteMapping("/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable UUID locationId) {
        locationService.delete(locationId);
    }


}

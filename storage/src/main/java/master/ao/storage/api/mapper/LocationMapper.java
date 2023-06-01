package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.LocationRequest;
import master.ao.storage.api.response.LocationResponse;
import master.ao.storage.core.domain.models.Location;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LocationMapper {

    private final ModelMapper mapper;

    public Location toLocation(LocationRequest request) {
        return mapper.map(request, Location.class);
    }

    public LocationResponse toLocationResponse(Location location) {
        return mapper.map(location, LocationResponse.class);
    }

    public List<LocationResponse> toLocationResponseList(List<Location> locations) {
        return locations.stream()
                .map(this::toLocationResponse)
                .collect(Collectors.toList());
    }
}
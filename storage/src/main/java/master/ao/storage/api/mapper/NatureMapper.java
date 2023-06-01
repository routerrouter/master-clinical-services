package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.NatureRequest;
import master.ao.storage.api.response.NatureResponse;
import master.ao.storage.core.domain.models.Nature;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NatureMapper {

    private final ModelMapper mapper;

    public Nature toNature(NatureRequest request) {
        return mapper.map(request, Nature.class);
    }

    public NatureResponse toNatureResponse(Nature Nature) {
        return mapper.map(Nature, NatureResponse.class);
    }

    public List<NatureResponse> toNatureResponseList(List<Nature> Natures) {
        return Natures.stream()
                .map(this::toNatureResponse)
                .collect(Collectors.toList());
    }
}
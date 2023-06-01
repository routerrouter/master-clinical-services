package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.StorageRequest;
import master.ao.storage.api.response.StorageResponse;
import master.ao.storage.core.domain.models.Storage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StorageMapper {

    private final ModelMapper mapper;

    public Storage toStorage(StorageRequest request) {
        return mapper.map(request, Storage.class);
    }

    public StorageResponse toStorageResponse(Storage storage) {
        return mapper.map(storage, StorageResponse.class);
    }

    public List<StorageResponse> toStorageResponseList(List<Storage> storages) {
        return storages.stream()
                .map(this::toStorageResponse)
                .collect(Collectors.toList());
    }
}
package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.TransferRequest;
import master.ao.storage.api.response.TransferResponse;
import master.ao.storage.core.domain.models.Transfer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransferMapper {

    private final ModelMapper mapper;

    public Transfer toTransfer(TransferRequest request) {
        return mapper.map(request, Transfer.class);
    }

    public TransferResponse toTransferResponse(Transfer transfer) {
        return mapper.map(transfer, TransferResponse.class);
    }

    public List<TransferResponse> toTransferResponseList(List<Transfer> transfers) {
        return transfers.stream()
                .map(this::toTransferResponse)
                .collect(Collectors.toList());
    }
}
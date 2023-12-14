package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.InvoiceRequest;
import master.ao.accountancy.api.responses.InvoiceResponse;
import master.ao.accountancy.domain.models.InvoiceProvider;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InvoiceMapper {

    private final ModelMapper mapper;

    public InvoiceProvider toInvoice(InvoiceRequest request) {
        return mapper.map(request, InvoiceProvider.class);
    }

    public InvoiceResponse toInvoiceResponse(InvoiceProvider invoice) {
        return mapper.map(invoice, InvoiceResponse.class);
    }

    public List<InvoiceResponse> toInvoiceResponseList(List<InvoiceProvider> invoiceList) {
        return invoiceList.stream()
                .map(this::toInvoiceResponse)
                .collect(Collectors.toList());
    }
}

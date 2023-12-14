package master.ao.accountancy.api.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.accountancy.api.mapper.InvoiceMapper;
import master.ao.accountancy.api.responses.AccountResponse;
import master.ao.accountancy.api.responses.InvoiceResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.InvoiceService;
import master.ao.accountancy.domain.services.UtilService;
import master.ao.accountancy.domain.specifications.SpecificationTemplate;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
@Tag(name = "Invoices", description = "The Invoice Provider API. Contains all operations that can be performed on a Invoice of Provider")
@RestController
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final UtilService utilService;
    private final InvoiceMapper mapper;

    @Operation(summary = "Get all invoices to provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found invoices",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<Page<Object>> getAllInvoicesByProvider(@PathVariable(name = "providerId") UUID providerId,
                                                                 @ParameterObject @PageableDefault(page = 0, size = 10, sort = "number", direction = Sort.Direction.ASC) Pageable pageable) {
        List<InvoiceResponse> invoiceResponses = invoiceService.findAllByProvider(providerId)
                .stream()
                .map(mapper::toInvoiceResponse)
                .collect(Collectors.toList());

        return utilService
                .getPageResponseEntity(pageable, new ArrayList<Object>(invoiceResponses));

    }

}

package master.ao.storage.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.mapper.CompanyMapper;
import master.ao.storage.api.request.CompanyRequest;
import master.ao.storage.api.response.CompanyResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.models.CompanyImage;
import master.ao.storage.core.domain.services.CompanyService;
import master.ao.storage.core.domain.wrappers.CompanyWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
@Tag(name = "Company", description = "The Company API. Contains all operations that can be performed on a company")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyMapper mapper;


    @Operation(summary = "Create or Update company details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Company saved!",
                    content = @Content(schema = @Schema(implementation = CompanyResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping()
    public ResponseEntity<CompanyResponse> saveCompany(@Valid @RequestBody CompanyRequest request,
                                                       @RequestParam UUID companyId) {
        log.debug("POST createCompany request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toCompany)
                .map(company -> companyService.createOrUpdateDetails(company, companyId))
                .map(mapper::toCompanyResponse)
                .map(companyResponse -> ResponseEntity.status(HttpStatus.CREATED).body(companyResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update image company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated company", content = @Content(schema = @Schema(implementation = CompanyResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping("/upload")
    public ResponseEntity<CompanyImage> updateImageUrl(@Valid @RequestBody CompanyWrapper wrapper) {
        CompanyImage companyImage = companyService.uploadImage(wrapper);
        return ResponseEntity.status(HttpStatus.OK).body(companyImage);
    }

    @Operation(summary = "Get a company details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the company",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping
    public ResponseEntity<CompanyResponse> findDetails() {
        return companyService.findDetails()
                .map(mapper::toCompanyResponse)
                .map(companyResponse -> ResponseEntity.status(HttpStatus.OK).body(companyResponse))
                .orElse(ResponseEntity.notFound().build());

    }



}

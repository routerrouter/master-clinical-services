package master.ao.accountancy.api.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.accountancy.api.mapper.DocumentMapper;
import master.ao.accountancy.api.requests.DocumentRequest;
import master.ao.accountancy.api.responses.DocumentResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.DocumentService;
import master.ao.accountancy.domain.services.UtilService;
import master.ao.accountancy.domain.specifications.SpecificationTemplate;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
@Tag(name = "Document", description = "The Document API. Contains all operations that can be performed on a Document")
@RestController
public class DocumentController {

    private final DocumentService documentService;
    private final UtilService utilService;
    private final DocumentMapper mapper;

    @Operation(summary = "Create Document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document created!",
                    content = @Content(schema = @Schema(implementation = DocumentResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping()
    private ResponseEntity<DocumentResponse> createDocument(@Valid @RequestBody DocumentRequest request) {
        log.debug("document requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toDocument)
                .map(document -> documentService.createDocument(document))
                .map(mapper::toDocumentResponse)
                .map(documentResponse -> ResponseEntity.status(HttpStatus.CREATED).body(documentResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Update Document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated document", content = @Content(schema = @Schema(implementation = DocumentResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{documentId}")
    public ResponseEntity<DocumentResponse> updateEntity(@Valid @RequestBody DocumentRequest request,
                                                       @Parameter(description = "id of document to be updated") @PathVariable("documentId") UUID documentId) {
        log.debug("PUT update Document request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toDocument)
                .map(document -> documentService.updateDocument(documentId,document))
                .map(mapper::toDocumentResponse)
                .map(documentResponse -> ResponseEntity.status(HttpStatus.OK).body(documentResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Documents",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<Object>> getAll(@ParameterObject SpecificationTemplate.DocumentSpec spec,
                                               @ParameterObject @PageableDefault(page = 0, size = 10, sort = "description", direction = Sort.Direction.ASC) Pageable pageable) {
        List<DocumentResponse> documentResponseList = documentService.findAllDocument(spec)
                .stream()
                .map(mapper::toDocumentResponse)
                .sorted((o1, o2) -> o1.getDescription().
                        compareTo(o2.getDescription()))
                .collect(Collectors.toList());

        return utilService
                .getPageResponseEntity(pageable, new ArrayList<Object>(documentResponseList));

    }

    @Operation(summary = "Get a document by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the document",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DocumentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Document class not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentResponse> fetchOrFail(@Parameter(description = "id of document to be searched")
                                                      @PathVariable("documentId") UUID documentId) {
        return documentService.fetchOrFail(documentId)
                .map(mapper::toDocumentResponse)
                .map(documentResponse -> ResponseEntity.status(HttpStatus.OK).body(documentResponse))
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted document!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Document class not found",
                    content = @Content)})
    @DeleteMapping("/{documentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Parameter(description = "id of document to be deleted") @PathVariable UUID documentId) {
        documentService.delete(documentId);
    }
}

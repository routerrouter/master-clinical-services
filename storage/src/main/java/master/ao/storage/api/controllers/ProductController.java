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
import master.ao.storage.api.mapper.ProductMapper;
import master.ao.storage.api.request.ProductRequest;
import master.ao.storage.api.response.ProductResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.services.ProductService;
import master.ao.storage.core.domain.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Tag(name = "Product", description = "The Product API. Contains all operations that can be performed on a Product")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper mapper;

    @Operation(summary = "Create product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created!",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping("")
    public ResponseEntity<ProductResponse> saveProduct(@Valid @RequestBody ProductRequest request) {
        log.debug("POST createProduct request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toProduct)
                .map(product -> productService.createProduct(product))
                .map(mapper::toProductResponse)
                .map(productResponse -> ResponseEntity.status(HttpStatus.CREATED).body(productResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated product", content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@Valid @RequestBody ProductRequest request,
                                                         @Parameter(description = "id of product to be updated")  @PathVariable("productId") UUID productId) {
        log.debug("PUT updateProduct request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toProduct)
                .map(product -> productService.updateProduct(product, productId))
                .map(mapper::toProductResponse)
                .map(productResponse -> ResponseEntity.status(HttpStatus.OK).body(productResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Products",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAll(SpecificationTemplate.ProductSpec spec,
                                                        @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                                                        @RequestParam(required = false) UUID categoryId,
                                                        @RequestParam(required = false) UUID productId,
                                                        @RequestParam(required = false) UUID natureId) {

        List<ProductResponse> productsList = new ArrayList<>();

        if (categoryId != null && productId != null) {
            productsList = productService.findAll(SpecificationTemplate.productCategoryId(categoryId)
                    .and(SpecificationTemplate.productCategoryId(productId))
                    .and(spec))
                    .stream()
                    .map(mapper::toProductResponse)
                    .sorted(getProductResponseComparator())
                    .collect(Collectors.toList());
        } else if (categoryId == null && productId != null) {
            productsList = productService.findAll(SpecificationTemplate.productGroupId(productId)
                    .and(spec))
                    .stream()
                    .map(mapper::toProductResponse)
                    .sorted(getProductResponseComparator())
                    .collect(Collectors.toList());
        } else if (categoryId != null && productId == null) {
            productsList = productService.findAll(SpecificationTemplate.productCategoryId(categoryId)
                    .and(spec))
                    .stream()
                    .map(mapper::toProductResponse)
                    .sorted(getProductResponseComparator())
                    .collect(Collectors.toList());
        } else {
            productsList = productService.findAll(spec)
                    .stream()
                    .map(mapper::toProductResponse)
                    .sorted(getProductResponseComparator())
                    .collect(Collectors.toList());
        }


        int start = (int) (pageable.getOffset() > productsList.size() ? productsList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > productsList.size() ? productsList.size()
                : (start + pageable.getPageSize()));
        Page<ProductResponse> productsPageList = new PageImpl<>(productsList.subList(start, end), pageable, productsList.size());

        return ResponseEntity.status(HttpStatus.OK).body(productsPageList);

    }

    private Comparator<ProductResponse> getProductResponseComparator() {
        return (o1, o2) -> o1.getName().
                compareTo(o2.getName());
    }

    @Operation(summary = "Get a product by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> fetchOrFail(@Parameter(description = "id of product to be searched")
                                                           @PathVariable("productId") UUID productId) {
        return productService.fetchOrFail(productId)
                .map(mapper::toProductResponse)
                .map(productResponse -> ResponseEntity.status(HttpStatus.OK).body(productResponse))
                .orElse(ResponseEntity.notFound().build());

    }


    @Operation(summary = "Delete a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted product!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content)})
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Parameter(description = "id of product to be deleted")
                           @PathVariable UUID productId) {
        productService.delete(productId);
    }


}

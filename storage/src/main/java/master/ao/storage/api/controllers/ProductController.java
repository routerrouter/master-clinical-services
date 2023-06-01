package master.ao.storage.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.mapper.ProductMapper;
import master.ao.storage.api.request.ProductRequest;
import master.ao.storage.api.response.ProductResponse;
import master.ao.storage.core.domain.services.ProductService;
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
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")

public class ProductController {

    private final ProductService productService;
    private final ProductMapper mapper;


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


    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@Valid @RequestBody ProductRequest request,
                                                         @PathVariable("productId") UUID productId) {
        log.debug("PUT updateProduct request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toProduct)
                .map(product -> productService.updateProduct(product, productId))
                .map(mapper::toProductResponse)
                .map(productResponse -> ResponseEntity.status(HttpStatus.OK).body(productResponse))
                .findFirst()
                .get();
    }


    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAll(SpecificationTemplate.ProductSpec spec,
                                                        @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                                                        @RequestParam(required = false) UUID categoryId,
                                                        @RequestParam(required = false) UUID groupId,
                                                        @RequestParam(required = false) UUID natureId) {

        List<ProductResponse> productsList = new ArrayList<>();

        if (categoryId != null && groupId != null) {
            productsList = productService.findAll(SpecificationTemplate.productCategoryId(categoryId)
                    .and(SpecificationTemplate.productGroupId(groupId))
                    .and(spec))
                    .stream()
                    .map(mapper::toProductResponse)
                    .sorted(getProductResponseComparator())
                    .collect(Collectors.toList());
        } else if (categoryId == null && groupId != null) {
            productsList = productService.findAll(SpecificationTemplate.productGroupId(groupId)
                    .and(spec))
                    .stream()
                    .map(mapper::toProductResponse)
                    .sorted(getProductResponseComparator())
                    .collect(Collectors.toList());
        } else if (categoryId != null && groupId == null) {
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


    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> fetchOrFail(@PathVariable("productId") UUID productId) {
        return productService.fetchOrFail(productId)
                .map(mapper::toProductResponse)
                .map(productResponse -> ResponseEntity.status(HttpStatus.OK).body(productResponse))
                .orElse(ResponseEntity.notFound().build());

    }


    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable UUID productId) {
        productService.delete(productId);
    }


}

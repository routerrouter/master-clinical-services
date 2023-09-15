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
import master.ao.accountancy.api.mapper.CategoryMapper;
import master.ao.accountancy.api.requests.CategoryRequest;
import master.ao.accountancy.api.responses.CategoryResponse;
import master.ao.accountancy.domain.exceptions.BussinessException;
import master.ao.accountancy.domain.services.CategoryService;
import master.ao.accountancy.domain.services.UtilService;
import master.ao.accountancy.domain.specifications.SpecificationTemplate;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
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

@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/categories")
@Tag(name = "Category", description = "The Category API. Contains all operations that can be performed on a Category")
@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final  UtilService utilService;
    private final  CategoryMapper mapper;

    @Operation(summary = "Create Category class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category class created!",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping()
    private ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        log.debug("category class requested:{} ", request.toString());

        return Stream.of(request)
                .map(mapper::toCategory)
                .map(category -> categoryService.saveCategory(category))
                .map(mapper::toCategoryResponse)
                .map(categoryResponse -> ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Update Category class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated category", content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateEntity(@Valid @RequestBody CategoryRequest request,
                                                       @Parameter(description = "id of category to be updated") @PathVariable("categoryId") UUID categoryId) {
        log.debug("PUT update Category class request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toCategory)
                .map(category -> categoryService.updateCategory(category,categoryId))
                .map(mapper::toCategoryResponse)
                .map(categoryResponse -> ResponseEntity.status(HttpStatus.OK).body(categoryResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Get all categorys class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Categories",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<Object>> getAll(@ParameterObject SpecificationTemplate.CategorySpec spec,
                                               @ParameterObject @PageableDefault(page = 0, size = 10, sort = "description", direction = Sort.Direction.ASC) Pageable pageable) {
        List<CategoryResponse> categoryResponseList = categoryService.findAllCategory(spec)
                .stream()
                .map(mapper::toCategoryResponse)
                .sorted((o1, o2) -> o1.getDescription().
                        compareTo(o2.getDescription()))
                .collect(Collectors.toList());

        return utilService
                .getPageResponseEntity(pageable, new ArrayList<Object>(categoryResponseList));

    }

    @Operation(summary = "Get a category by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the category",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> fetchOrFail(@Parameter(description = "id of category to be searched")
                                                      @PathVariable("categoryId") UUID categoryId) {
        return categoryService.fetchOrFail(categoryId)
                .map(mapper::toCategoryResponse)
                .map(categoryResponse -> ResponseEntity.status(HttpStatus.OK).body(categoryResponse))
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted category!",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content)})
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Parameter(description = "id of category to be deleted") @PathVariable UUID categoryId) {
        categoryService.delete(categoryId);
    }
}

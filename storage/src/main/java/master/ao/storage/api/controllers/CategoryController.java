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
import master.ao.storage.api.config.security.UserDetailsImpl;
import master.ao.storage.api.mapper.CategoryMapper;
import master.ao.storage.api.request.CategoryRequest;
import master.ao.storage.api.response.CategoryResponse;
import master.ao.storage.core.domain.exceptions.BussinessException;
import master.ao.storage.core.domain.services.CategoryService;
import master.ao.storage.core.domain.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
@Tag(name = "Category", description = "The Category API. Contains all operations that can be performed on a Category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper mapper;


    @Operation(summary = "Create category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created!",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PostMapping()
    public ResponseEntity<CategoryResponse> saveCategory(@Valid @RequestBody CategoryRequest request,
                                                         Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("POST createCategory request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toCategory)
                .map(category -> categoryService.save(category, userDetails.getUserId()))
                .map(mapper::toCategoryResponse)
                .map(categoryResponse -> ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse))
                .findFirst()
                .get();
    }


    @Operation(summary = "Update category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated category", content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @RequestBody CategoryRequest request,
                                                           @Parameter(description = "id of category to be updated")
                                                           @PathVariable("categoryId") UUID categoryId) {
        log.debug("PUT updateCategory request received {} ", request.toString());
        return Stream.of(request)
                .map(mapper::toCategory)
                .map(category -> categoryService.update(category, categoryId))
                .map(mapper::toCategoryResponse)
                .map(categoryResponse -> ResponseEntity.status(HttpStatus.OK).body(categoryResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Categories",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = BussinessException.class)))})
    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAll(SpecificationTemplate.CategorySpec spec,
                                                         @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        List<CategoryResponse> categoriesList = categoryService.findAll(spec)
                .stream()
                .map(mapper::toCategoryResponse)
                .sorted((o1, o2) -> o1.getName().
                        compareTo(o2.getName()))
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > categoriesList.size() ? categoriesList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > categoriesList.size() ? categoriesList.size()
                : (start + pageable.getPageSize()));
        Page<CategoryResponse> categoriesPageList = new PageImpl<>(categoriesList.subList(start, end), pageable, categoriesList.size());

        return ResponseEntity.status(HttpStatus.OK).body(categoriesPageList);

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

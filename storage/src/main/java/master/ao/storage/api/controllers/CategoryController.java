package master.ao.storage.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import master.ao.storage.api.config.security.UserDetailsImpl;
import master.ao.storage.api.mapper.CategoryMapper;
import master.ao.storage.api.request.CategoryRequest;
import master.ao.storage.api.response.CategoryResponse;
import master.ao.storage.core.domain.services.CategoryService;
import master.ao.storage.core.domain.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper mapper;


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


    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @RequestBody CategoryRequest request,
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


    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> fetchOrFail(@PathVariable("categoryId") UUID categoryId) {
        return categoryService.fetchOrFail(categoryId)
                .map(mapper::toCategoryResponse)
                .map(categoryResponse -> ResponseEntity.status(HttpStatus.OK).body(categoryResponse))
                .orElse(ResponseEntity.notFound().build());

    }


    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable UUID categoryId) {
        categoryService.delete(categoryId);
    }


}

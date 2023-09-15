package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.CategoryRequest;
import master.ao.accountancy.api.responses.CategoryResponse;
import master.ao.accountancy.domain.models.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CategoryMapper {

    private final ModelMapper mapper;

    public Category toCategory(CategoryRequest request) {
        return mapper.map(request, Category.class);
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return mapper.map(category, CategoryResponse.class);
    }

    public List<CategoryResponse> toCategoryResponseList(List<Category> categoryList) {
        return categoryList.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }
}

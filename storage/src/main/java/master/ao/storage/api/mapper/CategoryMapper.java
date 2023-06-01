package master.ao.storage.api.mapper;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.request.CategoryRequest;
import master.ao.storage.api.response.CategoryResponse;
import master.ao.storage.core.domain.models.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final ModelMapper mapper;

    public Category toCategory(CategoryRequest request) {
        return mapper.map(request, Category.class);
    }

    public CategoryResponse toCategoryResponse(Category Category) {
        return mapper.map(Category, CategoryResponse.class);
    }

    public List<CategoryResponse> toCategoryResponseList(List<Category> Categorys) {
        return Categorys.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }
}
package eCommerce.mapper;

import eCommerce.dto.response.CategoryResponse;
import eCommerce.model.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryResponse categoryResponse);


    @Mapping(target = "parentId",source = "parent.id")
    CategoryResponse toDto(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);


}

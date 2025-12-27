package eCommerce.mapper;

import eCommerce.dto.request.CategoryCreateRequest;
import eCommerce.dto.response.CategoryResponse;
import eCommerce.dto.response.ProductResponse;
import eCommerce.entity.Category;
import eCommerce.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryCreateRequest request);


    @Mapping(target = "parent.id",source = "parentId")
    @Mapping(target = "children",source = "children")
    CategoryResponse toDto(Category category);

    List<ProductResponse> toDto(List<Product> products);


}

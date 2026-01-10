package eCommerce.mapper;

import eCommerce.dto.request.ProductCreateRequest;
import eCommerce.dto.response.ProductResponse;
import eCommerce.model.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "favorites",ignore = true)
    Product toEntity(ProductCreateRequest productCreateRequest);


    @Mapping(target = "categoryId",source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "favoriteCount", expression = "java(product.getFavorites().size())")
    ProductResponse toDto(Product product);

    List<ProductResponse> toResponseList (List<Product> products );
}

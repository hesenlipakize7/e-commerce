package eCommerce.mapper;

import eCommerce.dto.response.ProductResponse;
import eCommerce.model.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "favorites",ignore = true)
    Product toEntity(ProductResponse productResponse);


    @Mapping(target = "categoryId",source = "category.id")
    ProductResponse toDto(Product product);

    List<ProductResponse> toResponseList (List<Product> products );
}

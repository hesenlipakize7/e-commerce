package eCommerce.mapper;

import eCommerce.dto.request.ProductCreateRequest;
import eCommerce.dto.response.ProductResponse;
import eCommerce.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "favorites",ignore = true)
    Product toEntity(ProductCreateRequest productCreateRequest);


    @Mapping(target = "categoryId",source = "category.id")
    ProductResponse toDto(Product product);

    List<ProductResponse> toDto(List<Product> products);
}

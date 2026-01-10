package eCommerce.mapper;

import eCommerce.dto.request.FavoriteCreateRequest;
import eCommerce.dto.response.FavoriteResponse;
import eCommerce.model.entity.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "product",ignore = true)
    Favorite toEntity(FavoriteCreateRequest request);

    @Mapping(target = "productId",source = "product.id")
    @Mapping(target = "favoriteId",source = "id")
    @Mapping(target = "productName",source = "product.name")
    @Mapping(target = "productPrice",source = "product.price")
    FavoriteResponse toResponseList(Favorite favorite);

    List<FavoriteResponse> toResponseList(List<Favorite> favorites);


}

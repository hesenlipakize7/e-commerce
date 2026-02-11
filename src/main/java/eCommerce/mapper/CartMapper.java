package eCommerce.mapper;

import eCommerce.dto.request.CartItemAddRequest;
import eCommerce.dto.response.CartItemResponse;
import eCommerce.dto.response.CartResponse;
import eCommerce.model.entity.Cart;
import eCommerce.model.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Cart toEntity(CartItemAddRequest cartItemAddRequest);

    @Mapping(target = "cartItemId", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    CartItemResponse toItemDto(CartItem cartItem);

    CartResponse toDto(Cart cart);
    List<CartItemResponse> toItemDtoList(List<CartItem> cartItems);


}

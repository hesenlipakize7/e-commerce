package eCommerce.mapper;

import eCommerce.dto.response.CartItemResponse;
import eCommerce.dto.response.CartResponse;
import eCommerce.entity.Cart;
import eCommerce.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price",target = "price")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    @Mapping(target = "totalPrice",ignore = true)
    CartResponse toCartResponse(Cart cart);
}

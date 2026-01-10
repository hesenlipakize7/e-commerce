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
    @Mapping(target = "product", ignore = true)
    Cart toEntity(CartItemAddRequest cartItemAddRequest);

    @Mapping(target = "cartItemId", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "totalAmount", expression = "java(cartItem.getProduct()" +
                                                 ".getPrice().multiply(BigDecimal" +
                                                 ".valueOf(cartItem.getQuantity())))")
    CartItemResponse toDto(CartItem cartItem);
    @Mapping(target = "totalPrice", expression = "java(calculateTotal(cart))")
    CartResponse toDto(Cart cart);
    List<CartItemResponse> toItemDtoList(List<CartItem> cartItems);

    default BigDecimal calculateTotal(Cart cart) {
        return cart.getCartItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

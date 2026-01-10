package eCommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


@Data
public class CartResponse {
    private Long cartId;
    private List<CartItemResponse> cartItems;
    private BigDecimal totalPrice;

    public static CartResponse empty() {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartItems(Collections.emptyList());
        cartResponse.setTotalPrice(BigDecimal.ZERO);
        return cartResponse;
    }
}

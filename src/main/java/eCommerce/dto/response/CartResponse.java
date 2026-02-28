package eCommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class CartResponse {
    private Long cartId;
    private List<CartItemResponse> cartItems;
    private BigDecimal totalPrice;

}

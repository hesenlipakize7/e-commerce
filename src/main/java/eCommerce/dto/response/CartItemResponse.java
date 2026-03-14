package eCommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class CartItemResponse {
    private long cartItemId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;

}

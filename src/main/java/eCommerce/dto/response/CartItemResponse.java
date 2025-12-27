package eCommerce.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class CartItemResponse {
    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
}

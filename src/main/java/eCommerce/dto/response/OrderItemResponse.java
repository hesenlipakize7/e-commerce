package eCommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;


@Data
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
}

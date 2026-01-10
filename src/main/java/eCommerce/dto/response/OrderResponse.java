package eCommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class OrderResponse {
    private Long orderId;
    private BigDecimal totalPrice;
    private LocalDate orderDate;

}

package eCommerce.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class OrderItemResponse {
    private String productName;
    private Double unitPrice;
    private Integer quantity;
}

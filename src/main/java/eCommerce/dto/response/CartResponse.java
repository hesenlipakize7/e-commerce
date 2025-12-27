package eCommerce.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class CartResponse {
    private Long cartId;
    private Double price;
}

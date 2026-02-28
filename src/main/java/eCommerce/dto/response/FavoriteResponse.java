package eCommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteResponse {
    private Long id;

    private Long productId;

    private String productName;

    private BigDecimal productPrice;

}

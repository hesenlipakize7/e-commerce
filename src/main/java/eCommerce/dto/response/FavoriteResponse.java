package eCommerce.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FavoriteResponse {
    private Long favoriteId;

    private Long productId;

    private String productName;

    private BigDecimal productPrice;

}

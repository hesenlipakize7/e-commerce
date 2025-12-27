package eCommerce.dto.response;

import lombok.Data;

@Data
public class FavoriteResponse {
    private Long id;

    private Long productId;
    private Long userId;

    private String productName;
    private Double productPrice;

}

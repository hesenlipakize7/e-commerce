package eCommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class FavoriteCreateRequest {
    @NotNull(message = "User id boş ola bilməz")
    private Long userId;

    @NotNull(message = "Product id boş ola bilməz")
    private Long productId;

}

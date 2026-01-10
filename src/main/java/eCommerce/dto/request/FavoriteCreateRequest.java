package eCommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FavoriteCreateRequest {

    @NotNull(message = "Product seçilməlidir")
    private Long productId;

}

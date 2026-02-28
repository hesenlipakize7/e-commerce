package eCommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteCreateRequest {

    @NotNull(message = "Product seçilməlidir")
    private Long productId;

}

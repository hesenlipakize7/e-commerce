package eCommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CartItemAddRequest {
    @NotNull(message = "Product seçilməlidir")
    private Long productId;

    @NotNull(message = "Miqdar boş ola bilməz")
    @Min(value = 1, message = "Miqdar minimum 1 olmalıdır")
    private Integer quantity;

}

package eCommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CartItemRequest {
    @NotNull(message = "ProductId boş ola bilməz")
    private Long productId;

    @NotNull(message = "Miqdar boş ola bilməz")
    @Min(value = 1, message = "Miqdar minimum 1 olmalıdır")
    private Integer quantity;

}

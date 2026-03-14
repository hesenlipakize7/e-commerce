package eCommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequest {
    @NotBlank(message = "Məhsul adı boş ola bilməz")
    private String name;

    @NotNull(message = "Qiymət boş ola bilməz")
    @Positive(message = "Qiymət müsbət olmalıdır")
    private BigDecimal price;

    @NotNull(message = "Stok sayı boş ola bilməz")
    @Positive(message = "Stok sayı müsbət olmalıdır")
    private Integer stock;

    @NotNull(message = "Kateqoriya seçilməlidir")
    private Long categoryId;
}

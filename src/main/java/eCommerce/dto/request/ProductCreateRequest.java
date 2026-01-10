package eCommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;


@Data
public class ProductCreateRequest {
    @NotBlank(message = "Məhsul adı boş ola bilməz")
    private String name;
    @NotNull(message = "Qiymət boş ola bilməz")
    private BigDecimal price;
    @NotNull(message = "Stok boş ola bilməz")
    private Integer stock;
    @NotNull(message = "Kateqoriya seçilməlidir")
    private Long categoryId;


}

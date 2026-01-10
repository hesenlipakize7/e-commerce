package eCommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CategoryCreateRequest {
    @NotBlank(message = "Kateqoriya adı boş ola bilməz")
    private String name;

    private Long parentId;
}

package eCommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

public class CategoryCreateRequest {
    @NotBlank(message = "Kateqoriya adı boş ola bilməz")
    private String name;

    private Long parentId;
}

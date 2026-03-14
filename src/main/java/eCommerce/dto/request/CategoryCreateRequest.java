package eCommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryCreateRequest {

    @NotBlank(message = "Kategoriya bos ola bilmez")
    private String name;

    @Positive(message = "Parent ID müsbət olmalıdır")
    private Long parentId;
}

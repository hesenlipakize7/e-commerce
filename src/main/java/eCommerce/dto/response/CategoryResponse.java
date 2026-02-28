package eCommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {
    private Long id;
    private String name;

    private Long parentId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CategoryResponse> subCategories;
}

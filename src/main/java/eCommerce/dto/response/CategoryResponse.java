package eCommerce.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class CategoryResponse {
    private Long id;
    private String name;

    private Long parentId;

    private List<CategoryResponse> children;
}

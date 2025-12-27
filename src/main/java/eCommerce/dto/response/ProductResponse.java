package eCommerce.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class ProductResponse {
    private Long id;
    private String name;
    private Double price;

    private Long parentId;
    private String categoryName;
}

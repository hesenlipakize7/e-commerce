package eCommerce.dto.update;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateRequest {
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;


}

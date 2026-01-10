package eCommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;


@Data
public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    private Long categoryId;
    private String categoryName;
}

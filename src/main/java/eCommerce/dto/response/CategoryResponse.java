package eCommerce.dto.response;

import lombok.*;

import java.util.List;


@Data
public class CategoryResponse {
    private Long id;
    private String name;

    private Long parentId;

    private List<CategoryResponse> children;
}

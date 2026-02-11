package eCommerce.serviceLayer.service;


import eCommerce.dto.response.CategoryResponse;
import eCommerce.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getChildren(Long parentId);
    CategoryResponse getById(Long id);
    Page<ProductResponse> getProducts(Long categoryId, Pageable pageable);
    List<CategoryResponse> search(String keyword);

}

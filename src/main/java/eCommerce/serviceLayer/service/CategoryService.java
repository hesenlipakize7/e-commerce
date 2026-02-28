package eCommerce.serviceLayer.service;


import eCommerce.dto.request.CategoryCreateRequest;
import eCommerce.dto.response.CategoryResponse;
import eCommerce.dto.response.ProductResponse;
import eCommerce.dto.update.CategoryUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getSubCategories(Long parentId);
    CategoryResponse getById(Long id);
    Page<ProductResponse> getProducts(Long categoryId, Pageable pageable);
    List<CategoryResponse> search(String keyword);

    CategoryResponse createCategory(CategoryCreateRequest createRequest);
    CategoryResponse updateCategory(Long id,CategoryUpdateRequest updateRequest);
    void deleteCategory(Long id);

}

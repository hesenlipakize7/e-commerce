package eCommerce.serviceLayer.service;

import eCommerce.dto.request.CategoryCreateRequest;
import eCommerce.dto.response.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CategoryResponse createCategory(CategoryCreateRequest request);
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    void deleteCategory(Long id);
}

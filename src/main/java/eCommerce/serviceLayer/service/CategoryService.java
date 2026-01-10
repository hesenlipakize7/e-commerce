package eCommerce.serviceLayer.service;


import eCommerce.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getRootCategories();
    List<CategoryResponse> getChildren(Long parentId);
    CategoryResponse getById(Long id);

}

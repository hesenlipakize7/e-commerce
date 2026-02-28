package eCommerce.serviceLayer.service;


import eCommerce.dto.request.ProductCreateRequest;
import eCommerce.dto.response.ProductResponse;
import eCommerce.dto.update.ProductUpdateRequest;

import java.util.List;

public interface ProductService {
    ProductResponse getById(Long productId);

    List<ProductResponse> getAll();

    List<ProductResponse> getByCategoryId(Long categoryId);

    List<ProductResponse> search(String keyword);

    ProductResponse createProduct(ProductCreateRequest productCreateRequest);
    ProductResponse updateProduct(Long id, ProductUpdateRequest productUpdateRequest);
    void deleteProduct(Long id);
}

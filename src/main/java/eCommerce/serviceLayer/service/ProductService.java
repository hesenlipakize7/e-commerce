package eCommerce.serviceLayer.service;


import eCommerce.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse getById(Long productId);

    List<ProductResponse> getAll();

    List<ProductResponse> getByCategoryId(Long categoryId);

    List<ProductResponse> search(String keyword);
}

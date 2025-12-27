package eCommerce.serviceLayer.service;

import eCommerce.dto.request.ProductCreateRequest;
import eCommerce.dto.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    ProductResponse createProduct(ProductCreateRequest request);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id);
    void deleteProduct(Long id);
}

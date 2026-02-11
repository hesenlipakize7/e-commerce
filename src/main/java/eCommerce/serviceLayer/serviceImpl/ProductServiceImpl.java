package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.response.ProductResponse;
import eCommerce.exception.NotFoundException;
import eCommerce.model.entity.Product;
import eCommerce.mapper.CategoryMapper;
import eCommerce.mapper.ProductMapper;
import eCommerce.repository.CategoryRepository;
import eCommerce.repository.ProductRepository;
import eCommerce.serviceLayer.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public List<ProductResponse> getAll() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        log.info("Fetching all products successful");
        return productMapper.toResponseList(products);
    }


    @Override
    public ProductResponse getById(Long productId) {
        log.info("Fetching product by id. productId={}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found. productId={}", productId);
                    return new NotFoundException("Product not found");
                });
        log.info("Fetching product successful");
        return productMapper.toDto(product);
    }


    @Override
    public List<ProductResponse> getByCategoryId(Long categoryId) {
        log.info("Fetching products by category. categoryId={}", categoryId);
        List<Product> products = productRepository.findByCategoryId(categoryId);
        if (products.isEmpty()) {
            log.warn("No products found for category. categoryId={}", categoryId);
        } else {
            log.debug("Products found for category");
        }
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> search(String keyword) {
        log.info("Product search requested");
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        log.debug("Search result count for keyword ");
        return productMapper.toResponseList(products);
    }
}

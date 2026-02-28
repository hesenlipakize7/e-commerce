package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.request.ProductCreateRequest;
import eCommerce.dto.response.ProductResponse;
import eCommerce.dto.update.ProductUpdateRequest;
import eCommerce.exception.NotFoundException;
import eCommerce.model.entity.Category;
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
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        log.info("Admin creating product : {}", productCreateRequest);
        Category category = categoryRepository.findById(productCreateRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        Product product = new Product();
        product.setName(productCreateRequest.getName());
        product.setCategory(category);
        product.setPrice(productCreateRequest.getPrice());
        product.setStock(productCreateRequest.getStock());
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully: {}", savedProduct);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        log.info("Admin updating product: id={}",id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        if (productUpdateRequest.getName() != null) {
            product.setName(productUpdateRequest.getName());
        }
        if (productUpdateRequest.getPrice() != null) {
            product.setPrice(productUpdateRequest.getPrice());
        }
        if (productUpdateRequest.getStock() != null) {
            product.setStock(productUpdateRequest.getStock());
        }
        if (productUpdateRequest.getCategoryId() != null) {
            product.setCategory(categoryRepository.findById(productUpdateRequest.getCategoryId())
            .orElseThrow(() -> new NotFoundException("Category not found")));
            product.setCategory(product.getCategory());
        }
        log.info("Product updated successfully: id={}", id);
        return productMapper.toDto(product);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Admin deleting product : {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.delete(product);
        log.info("Product deleted successfully: id={}", id);
    }

    @Override
    public List<ProductResponse> getAll() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        log.info("Fetching all products successful");
        return productMapper.toResponseList(products);
    }


    @Override
    public ProductResponse getById(Long productId) {
        log.info("Fetching product by id.");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found.");
                    return new NotFoundException("Product not found");
                });
        log.info("Fetching product successful");
        return productMapper.toDto(product);
    }


    @Override
    public List<ProductResponse> getByCategoryId(Long categoryId) {
        log.info("Fetching products by category.");
        List<Product> products = productRepository.findByCategory_Id(categoryId);
        if (products.isEmpty()) {
            log.warn("No products found for category.");
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

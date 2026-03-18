package eCommerce.service.impl;

import eCommerce.dto.request.ProductCreateRequest;
import eCommerce.dto.response.ProductResponse;
import eCommerce.dto.update.ProductUpdateRequest;
import eCommerce.exception.NotFoundException;
import eCommerce.model.entity.Category;
import eCommerce.model.entity.Product;
import eCommerce.mapper.ProductMapper;
import eCommerce.repository.CategoryRepository;
import eCommerce.repository.ProductRepository;
import eCommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        log.info("Create product request. name={}, categoryId={}, price={}, stock={}",
                productCreateRequest.getName(), productCreateRequest.getCategoryId(),
                productCreateRequest.getPrice(), productCreateRequest.getStock());
        Category category = categoryRepository.findById(productCreateRequest.getCategoryId())
                .orElseThrow(() -> {
                    log.warn("Category not found while creating product. categoryId={}", productCreateRequest.getCategoryId());
                    return new NotFoundException("Category not found");
                });
        Product product = new Product();
        product.setName(productCreateRequest.getName());
        product.setCategory(category);
        product.setPrice(productCreateRequest.getPrice());
        product.setStock(productCreateRequest.getStock());
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully. productId={}, name={}, categoryId={}",
                savedProduct.getId(), productCreateRequest.getName(), productCreateRequest.getCategoryId());
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        log.info("Update product request. productId={}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found for update. productId={}", id);
                    return new NotFoundException("Product not found");
                });
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
                    .orElseThrow(() -> {
                        log.warn("Category not found while updating product. categoryId={}", productUpdateRequest.getCategoryId());
                        return new NotFoundException("Category not found");
                    }));
            product.setCategory(product.getCategory());
        }
        log.info("Product updated successfully. productId={}", id);
        return productMapper.toDto(product);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Delete product request. productId={}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found for delete. productId={}", id);
                    return new NotFoundException("Product not found");
                });
        productRepository.delete(product);
        log.info("Product deleted successfully. productId={}", id);
    }

    @Override
    public List<ProductResponse> getAll() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        log.info("Products fetched successfully. count={}", products.size());
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
        log.info("Product fetched successfully. productId={}", productId);
        return productMapper.toDto(product);
    }


    @Override
    public List<ProductResponse> getByCategoryId(Long categoryId) {
        log.info("Fetching products by categoryId={}", categoryId);
        List<Product> products = productRepository.findByCategory_Id(categoryId);
        if (products.isEmpty()) {
            log.warn("No products found for categoryId={}", categoryId);
        } else {
            log.info("Products fetched for categoryId={}, count={}", categoryId, products.size());
        }
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> search(String keyword) {
        log.info("Product search request. keyword={}", keyword);
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        log.info("Product search completed. keyword={}, resultCount={}", keyword, products.size());
        return productMapper.toResponseList(products);
    }
}

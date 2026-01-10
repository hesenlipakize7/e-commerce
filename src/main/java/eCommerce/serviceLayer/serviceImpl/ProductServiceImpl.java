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
        List<Product> products = productRepository.findAll();
        return productMapper.toResponseList(products);
    }


    @Override
    public ProductResponse getById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return productMapper.toDto(product);
    }


    @Override
    public List<ProductResponse> getByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> search(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        return productMapper.toResponseList(products);
    }
}

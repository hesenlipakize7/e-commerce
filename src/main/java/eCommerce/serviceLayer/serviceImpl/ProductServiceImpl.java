package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.request.ProductCreateRequest;
import eCommerce.dto.response.ProductResponse;
import eCommerce.entity.Category;
import eCommerce.entity.Product;
import eCommerce.mapper.CategoryMapper;
import eCommerce.mapper.ProductMapper;
import eCommerce.repository.CategoryRepository;
import eCommerce.repository.ProductRepository;
import eCommerce.serviceLayer.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategoriya tapilmadi"));
        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDto(products);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product=productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product tapilmadi"));
        return productMapper.toDto(product);
    }


    @Override
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
            throw new RuntimeException("Kategoriya tapilmadi");
        }
        productRepository.deleteById(id);
    }
}

package eCommerce.service;
import eCommerce.dto.request.ProductCreateRequest;
import eCommerce.dto.response.ProductResponse;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.ProductMapper;
import eCommerce.model.entity.Category;
import eCommerce.model.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import eCommerce.repository.ProductRepository;
import eCommerce.repository.CategoryRepository;
import eCommerce.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Category category;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Phone");
        product.setPrice(BigDecimal.valueOf(1000));
        product.setStock(10);
        product.setCategory(category);

        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Phone");
        productResponse.setPrice(BigDecimal.valueOf(1000));
        productResponse.setStock(10);
        productResponse.setCategoryId(1L);
    }

    @Test
    void createProduct_success() {

        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Phone");
        request.setCategoryId(1L);
        request.setPrice(BigDecimal.valueOf(1000));
        request.setStock(10);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productResponse);

        ProductResponse result = productService.createProduct(request);

        assertNotNull(result);
        assertEquals("Phone", result.getName());

        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
        verify(productMapper).toDto(product);
    }

    @Test
    void createProduct_categoryNotFound() {

        ProductCreateRequest request = new ProductCreateRequest();
        request.setCategoryId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                productService.createProduct(request));
    }

    @Test
    void getById_success() {

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productResponse);

        ProductResponse result = productService.getById(1L);

        assertEquals(1L, result.getId());

        verify(productRepository).findById(1L);
        verify(productMapper).toDto(product);
    }

    @Test
    void getById_productNotFound() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                productService.getById(1L));
    }

    @Test
    void deleteProduct_success() {

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void getAll_success() {

        List<Product> products = List.of(product);
        List<ProductResponse> responses = List.of(productResponse);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toResponseList(products)).thenReturn(responses);

        List<ProductResponse> result = productService.getAll();

        assertEquals(1, result.size());

        verify(productRepository).findAll();
        verify(productMapper).toResponseList(products);
    }

    @Test
    void search_success() {

        List<Product> products = List.of(product);
        List<ProductResponse> responses = List.of(productResponse);

        when(productRepository.findByNameContainingIgnoreCase("phone"))
                .thenReturn(products);

        when(productMapper.toResponseList(products))
                .thenReturn(responses);

        List<ProductResponse> result = productService.search("phone");

        assertEquals(1, result.size());

        verify(productRepository)
                .findByNameContainingIgnoreCase("phone");
    }

}

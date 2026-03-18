package eCommerce.service;


import eCommerce.dto.request.CategoryCreateRequest;
import eCommerce.dto.response.CategoryResponse;
import eCommerce.dto.response.ProductResponse;
import eCommerce.dto.update.CategoryUpdateRequest;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.CategoryMapper;
import eCommerce.mapper.ProductMapper;
import eCommerce.model.entity.*;
import eCommerce.repository.CategoryRepository;
import eCommerce.repository.ProductRepository;
import eCommerce.service.impl.CategoryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private Category parent;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {

        parent = new Category();
        parent.setId(1L);
        parent.setName("Electronics");

        category = new Category();
        category.setId(2L);
        category.setName("Phones");
        category.setParent(parent);

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(2L);
        categoryResponse.setName("Phones");
    }

    @Test
    void createCategory_success() {

        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setName("Phones");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.createCategory(request);

        assertNotNull(result);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_parentNotFound() {

        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setName("Phones");
        request.setParentId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.createCategory(request));
    }

    @Test
    void updateCategory_success() {

        CategoryUpdateRequest request = new CategoryUpdateRequest();
        request.setName("Updated");

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.updateCategory(2L, request);

        assertNotNull(result);
        verify(categoryRepository).findById(2L);
    }

    @Test
    void deleteCategory_success() {

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(2L);

        verify(categoryRepository).delete(category);
    }

    @Test
    void getProducts_success() {

        Pageable pageable = PageRequest.of(0, 10);

        Product product = new Product();
        Page<Product> productPage = new PageImpl<>(List.of(product));

        ProductResponse productResponse = new ProductResponse();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(productRepository.findAllByCategoryId(1L, pageable)).thenReturn(productPage);
        when(productMapper.toDto(product)).thenReturn(productResponse);

        Page<ProductResponse> result = categoryService.getProducts(1L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void search_success() {

        List<Category> categories = List.of(parent);
        List<CategoryResponse> responses = List.of(categoryResponse);

        when(categoryRepository.findByNameContainingIgnoreCase("Electronics"))
                .thenReturn(categories);

        when(categoryMapper.toResponseList(categories))
                .thenReturn(responses);

        List<CategoryResponse> result = categoryService.search("Electronics");

        assertEquals(1, result.size());
    }

    @Test
    void search_keywordEmpty() {

        assertThrows(BadRequestException.class,
                () -> categoryService.search(""));
    }

    @Test
    void getSubCategories_success() {

        Category parent = new Category();
        parent.setId(1L);

        Category child = new Category();
        child.setId(2L);

        parent.setSubCategories(List.of(child));

        when(categoryRepository.findWithSubCategoriesById(1L))
                .thenReturn(Optional.of(parent));

        when(categoryMapper.toResponseList(parent.getSubCategories()))
                .thenReturn(List.of(new CategoryResponse()));

        List<CategoryResponse> result = categoryService.getSubCategories(1L);

        assertEquals(1, result.size());

        verify(categoryRepository).findWithSubCategoriesById(1L);
        verify(categoryMapper).toResponseList(parent.getSubCategories());
    }
    @Test
    void getSubCategories_notFound() {

        when(categoryRepository.findWithSubCategoriesById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.getSubCategories(1L));
    }

    @Test
    void getById_success() {

        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findWithSubCategoriesById(1L))
                .thenReturn(Optional.of(category));

        when(categoryMapper.toDto(category))
                .thenReturn(new CategoryResponse());

        CategoryResponse result = categoryService.getById(1L);

        assertNotNull(result);

        verify(categoryRepository).findWithSubCategoriesById(1L);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void getById_notFound() {

        when(categoryRepository.findWithSubCategoriesById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.getById(1L));
    }

}

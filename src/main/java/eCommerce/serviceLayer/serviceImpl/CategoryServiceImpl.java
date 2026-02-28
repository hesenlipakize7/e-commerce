package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.request.CategoryCreateRequest;
import eCommerce.dto.response.CategoryResponse;
import eCommerce.dto.response.ProductResponse;
import eCommerce.dto.update.CategoryUpdateRequest;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.ProductMapper;
import eCommerce.model.entity.Category;
import eCommerce.mapper.CategoryMapper;
import eCommerce.model.entity.Product;
import eCommerce.repository.CategoryRepository;
import eCommerce.repository.ProductRepository;
import eCommerce.serviceLayer.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;


    @Override
    public CategoryResponse createCategory(CategoryCreateRequest createRequest) {
        log.info("Admin creating a category: {}", createRequest.getName());
        Category category = new Category();
        category.setName(createRequest.getName());
        if (createRequest.getParentId() != null) {
            Category parent = categoryRepository.findById(createRequest.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent category not found"));
            category.setParent(parent);
        }
        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully : id={}", savedCategory.getId());
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest updateRequest) {
        log.info("Admin updating a category: {}", updateRequest.getName());
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(updateRequest.getName());
        if (updateRequest.getParentId() != null) {
            Category parent = categoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Parent category not found"));
            category.setParent(parent);
        }
        log.info("Category updated successfully : id={}", category.getId());
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Admin deleting a category: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.delete(category);
        log.info("Category deleted successfully : id={}", category.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(Long categoryId, Pageable pageable) {
        log.info("Fetching products for category.");
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        Page<Product> products = productRepository.findAllByCategoryId(category.getId(), pageable);
        log.info("Products fetched successfully.");
        return products.map(productMapper::toDto);
    }

    @Override
    public List<CategoryResponse> search(String keyword) {
        log.info("Searching for categories for keyword: {} ", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Keyword cannot be empty");
        }
        List<Category> found = categoryRepository.findByNameContainingIgnoreCase(keyword);

        List<Category> roots = found.stream()
                .filter(category -> category.getParent() == null)
                .toList();

        log.info("Category search completed.");
        return categoryMapper.toResponseList(roots);
    }

    @Override
    public List<CategoryResponse> getSubCategories(Long parentId) {
        log.info("Fetching sub categories. ");
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> {
                    log.warn("Parent category not found.");
                    return new NotFoundException("Category Not Found");
                });
        log.debug("Sub category count for parentId.");
        return categoryMapper.toResponseList(parentCategory.getSubCategories());
    }

    @Override
    public CategoryResponse getById(Long id) {
        log.info("Fetching category by id.");
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found.");
                    return new NotFoundException("Category Not Found");
                });
        log.debug("Category found. ");
        return categoryMapper.toDto(category);
    }
}

package eCommerce.service.impl;

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
import eCommerce.service.CategoryService;
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
        log.info("Create category request received. name={}, parentId={}", createRequest.getName(), createRequest.getParentId());
        Category category = new Category();
        category.setName(createRequest.getName());
        if (createRequest.getParentId() != null) {
            Category parent = categoryRepository.findById(createRequest.getParentId())
                    .orElseThrow(() -> {
                        log.warn("Parent category not found. parentId={}", createRequest.getParentId());
                        return new NotFoundException("Parent category not found");
                    });
            category.setParent(parent);
        }
        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully. categoryId={}, name={}", category.getId(), category.getName());
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest updateRequest) {
        log.info("Update category request. categoryId={}, newName={}, parentId={}", id, updateRequest.getName(), updateRequest.getParentId());
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found for update. categoryId={}", id);
                    return new NotFoundException("Category not found");
                });
        category.setName(updateRequest.getName());
        if (updateRequest.getParentId() != null) {
            Category parent = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Parent category not found: parentId={}", updateRequest.getParentId());
                        return new NotFoundException("Parent category not found");
                    });
            category.setParent(parent);
        }
        log.info("Category updated successfully. categoryId={}", category.getId());
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Delete category request. categoryId={}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found for delete. categoryId={}", id);
                    return new NotFoundException("Category not found");
                });
        categoryRepository.delete(category);
        log.info("Category deleted successfully. categoryId={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(Long categoryId, Pageable pageable) {
        log.info("Fetching products for categoryId={}, page={}, size={}", categoryId, pageable.getOffset(), pageable.getPageSize());
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category not found while fetching products. categoryId={}", categoryId);
                    return new NotFoundException("Category not found");
                });
        Page<Product> products = productRepository.findAllByCategoryId(category.getId(), pageable);
        log.debug("Products fetched. categoryId={}, totalElements={}", categoryId, products.getTotalElements());
        return products.map(productMapper::toDto);
    }

    @Override
    public List<CategoryResponse> search(String keyword) {
        log.info("Category search request. keyword={} ", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("Category search failed. Keyword is empty.");
            throw new BadRequestException("Keyword cannot be empty");
        }
        List<Category> found = categoryRepository.findByNameContainingIgnoreCase(keyword);

        List<Category> roots = found.stream()
                .filter(category -> category.getParent() == null)
                .toList();

        log.info("Category search completed. keyword={}, resultCount={}", keyword, found.size());
        return categoryMapper.toResponseList(roots);
    }

    @Override
    public List<CategoryResponse> getSubCategories(Long parentId) {
        log.info("Fetching sub categories for parentId={}", parentId);
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> {
                    log.warn("Parent category not found : parentId={}", parentId);
                    return new NotFoundException("Category Not Found");
                });
        log.debug("Sub categories fetched. parentId={}, count={}", parentId, parentCategory.getSubCategories().size());
        return categoryMapper.toResponseList(parentCategory.getSubCategories());
    }

    @Override
    public CategoryResponse getById(Long id) {
        log.info("Fetching category by id. categoryId={}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found. categoryId={}", id);
                    return new NotFoundException("Category Not Found");
                });
        log.debug("Category fetched successfully. categoryId={}", category.getId());
        return categoryMapper.toDto(category);
    }
}

package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.response.CategoryResponse;
import eCommerce.dto.response.ProductResponse;
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
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        Page<Product> products = productRepository.findAllByCategoryId(category.getId(),pageable);
        return products.map(productMapper::toDto);
    }

    @Override
    public List<CategoryResponse> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Keyword cannot be empty");
        }
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(keyword);
        return categoryMapper.toResponseList(categories);
    }

    @Override
    public List<CategoryResponse> getChildren(Long parentId) {
        log.info("Fetching child categories. parentId={}", parentId);
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> {
                    log.warn("Parent category not found. parentId={}", parentId);
                    return new NotFoundException("Category Not Found");
                });
        log.debug("Child category count for parentId={}: {}", parentId, parentCategory);
        return categoryMapper.toResponseList(parentCategory.getChildren());
    }

    @Override
    public CategoryResponse getById(Long id) {
        log.info("Fetching category by id. categoryId={}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found. categoryId={}", id);
                    return new NotFoundException("Category Not Found");
                });
        log.debug("Category found. categoryId={}, name={}", category.getId(), category.getName());
        return categoryMapper.toDto(category);
    }
}

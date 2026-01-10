package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.response.CategoryResponse;
import eCommerce.exception.NotFoundException;
import eCommerce.model.entity.Category;
import eCommerce.mapper.CategoryMapper;
import eCommerce.repository.CategoryRepository;
import eCommerce.serviceLayer.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    public List<CategoryResponse> getRootCategories() {
        return categoryMapper.toResponseList(categoryRepository.findByParentIsNull());
    }

    @Override
    public List<CategoryResponse> getChildren(Long parentId) {
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));
        return categoryMapper.toResponseList(parentCategory.getChildren());
    }

    @Override
    public CategoryResponse getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Category not found")));
    }
}

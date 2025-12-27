package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.request.CategoryCreateRequest;
import eCommerce.dto.response.CategoryResponse;
import eCommerce.entity.Category;
import eCommerce.mapper.CategoryMapper;
import eCommerce.repository.CategoryRepository;
import eCommerce.serviceLayer.service.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Bu kateqoriya artıq mövcuddur");
        }
        Category category=categoryMapper.toEntity(request);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
       return  categoryRepository.findAll()
               .stream().map(categoryMapper::toDto)
               .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category=categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Kateqoriya tapılmadı"));
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            throw new RuntimeException("Kateqoriya yoxdur");
        }
        categoryRepository.deleteById(id);
    }
}

package eCommerce.controller;

import eCommerce.dto.request.CategoryCreateRequest;
import eCommerce.dto.response.CategoryResponse;
import eCommerce.dto.update.CategoryUpdateRequest;
import eCommerce.serviceLayer.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    CategoryResponse createCategory(@Valid @RequestBody CategoryCreateRequest createRequest) {
        return categoryService.createCategory(createRequest);
    }

    @PutMapping("/{id}")
    CategoryResponse updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryUpdateRequest updateRequest) {
        return categoryService.updateCategory(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}

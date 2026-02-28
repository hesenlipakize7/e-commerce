package eCommerce.controller;

import eCommerce.dto.response.CategoryResponse;
import eCommerce.dto.response.ProductResponse;
import eCommerce.serviceLayer.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping("{id}/products")
    public Page<ProductResponse> getProducts(@PathVariable Long id, Pageable pageable) {
        return categoryService.getProducts(id, pageable);
    }

    @GetMapping("/search")
    public List<CategoryResponse> search(@RequestParam String keyword) {
        return categoryService.search(keyword);
    }

    @GetMapping("{id}")
    public CategoryResponse getById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @GetMapping("/{id}/child")
    public List<CategoryResponse> getSubCategories(@PathVariable Long id) {
        return categoryService.getSubCategories(id);
    }
}

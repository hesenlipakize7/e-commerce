package eCommerce.controller;

import eCommerce.dto.request.ProductCreateRequest;
import eCommerce.dto.response.ProductResponse;
import eCommerce.dto.update.ProductUpdateRequest;
import eCommerce.serviceLayer.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {
    private final ProductService productService;

    @PostMapping
    public ProductResponse createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return productService.createProduct(productCreateRequest);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@Valid @RequestBody ProductUpdateRequest productUpdateRequest, @PathVariable Long id) {
        return productService.updateProduct(id, productUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}

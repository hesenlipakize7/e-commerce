package eCommerce.controller;

import eCommerce.dto.response.CartResponse;
import eCommerce.serviceLayer.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;


    @GetMapping
    public CartResponse getMyCart() {
        return cartService.getMyCart();
    }

    @PostMapping("/add/items")
    public void addProductToCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.addProductToCart(productId, quantity);
    }

    @PutMapping("/items/{productId}")
    public void updateProductQuantity(@PathVariable Long productId, @RequestParam int quantity) {
        cartService.updateProductQuantity(productId, quantity);
    }

    @DeleteMapping("/items/{productId}")
    public void removeProductFromCart(@PathVariable Long productId) {
        cartService.removeProductFromCart(productId);
    }
}

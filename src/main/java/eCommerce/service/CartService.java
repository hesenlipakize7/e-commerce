package eCommerce.service;

import eCommerce.dto.response.CartResponse;

public interface CartService {
    CartResponse getMyCart();

    void addProductToCart(Long productId, Integer quantity);

    void removeProductFromCart(Long productId);

    void updateProductQuantity(Long productId, Integer quantity);
}

package eCommerce.serviceLayer.service;

import eCommerce.dto.response.CartResponse;

public interface CartService {
    CartResponse getMyCart();
    void addProductToCart(Long productId, int quantity);
    void removeProductFromCart(Long productId);
    void updateProductQuantity(Long productId, int quantity);
}

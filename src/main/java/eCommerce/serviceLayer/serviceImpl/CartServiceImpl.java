package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.response.CartResponse;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.model.entity.Cart;
import eCommerce.model.entity.CartItem;
import eCommerce.model.entity.Product;
import eCommerce.model.entity.User;
import eCommerce.mapper.CartMapper;
import eCommerce.repository.CartItemRepository;
import eCommerce.repository.CartRepository;
import eCommerce.repository.ProductRepository;
import eCommerce.serviceLayer.service.CartService;
import eCommerce.serviceLayer.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CartMapper cartMapper;


    @Override
    public void addProductToCart(Long productId, int quantity) {
        User user = userService.getAuthenticatedUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createCart(user));
        Optional<CartItem> optionalItem = cartItemRepository.findByCartIdAndProductId(productId, cart.getId());
        if (optionalItem.isPresent()) {
            CartItem cartItem = optionalItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);
        }
    }

    @Override
    public void removeProductFromCart(Long productId) {
        User user = userService.getAuthenticatedUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("Cart is empty"));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(productId, cart.getId())
                .orElseThrow(() -> new NotFoundException("Product not in cart"));
        cart.getCartItems().remove(cartItem);
    }

    @Override
    public void updateProductQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }
        User user = userService.getAuthenticatedUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(productId, cart.getId())
                .orElseThrow(() -> new NotFoundException("Product not in cart"));
        cartItem.setQuantity(quantity);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getMyCart() {
        User user = userService.getAuthenticatedUser();
        return cartRepository.findByUserId(user.getId())
                .map(cartMapper::toDto)
                .orElseGet(CartResponse::empty);
    }

    private Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }
}

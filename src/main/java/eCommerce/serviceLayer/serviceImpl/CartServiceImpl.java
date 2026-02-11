package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.response.CartItemResponse;
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

import java.math.BigDecimal;
import java.util.List;
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
        log.info("Remove product from cart requested. userId={}, productId={}", user.getId(), productId);
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> {
                    log.warn("Remove product failed. Cart not found. userId={}", user.getId());
                    return new BadRequestException("Cart is empty");
                });

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(productId, cart.getId())
                .orElseThrow(() -> {
                    log.warn("Product not found in cart. cartId={}, productId={}", cart.getId(), productId);
                    return new NotFoundException("Product not in cart");
                });
        cart.getCartItems().remove(cartItem);
        log.info("Product removed from cart successfully. userId={}, productId={}", user.getId(), productId);
    }

    @Override
    public void updateProductQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            log.warn("Invalid quantity update attempted. productId={}, quantity={}", productId, quantity);
            throw new BadRequestException("Quantity must be greater than 0");
        }
        User user = userService.getAuthenticatedUser();
        log.info("Update product quantity requested. userId={}, productId={}, quantity={}", user.getId(), productId, quantity);
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> {
                    log.warn("Cart not found during quantity update. userId={}", user.getId());
                    return new NotFoundException("Cart not found");
                });
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(productId, cart.getId())
                .orElseThrow(() -> {
                    log.warn("Product not found in cart during quantity update. cartId={}, productId={}", cart.getId(), productId);
                    return new NotFoundException("Product not in cart");
                });
        cartItem.setQuantity(quantity);
        log.debug("Product quantity updated in cart. cartId={}, productId={}, quantity={}", cart.getId(), productId, quantity);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getMyCart() {
        User user = userService.getAuthenticatedUser();
        log.info("Fetching cart for user. userId={}", user.getId());
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("Cart is empty"));

        List<CartItemResponse> items = cartMapper.toItemDtoList(cart.getCartItems());

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {
            BigDecimal itemTotal =
                    item.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));

            totalPrice = totalPrice.add(itemTotal);
        }

        CartResponse response = new CartResponse();
        response.setCartItems(items);
        response.setTotalPrice(totalPrice);

        return response;

    }

    private Cart createCart(User user) {
        log.info("Creating new cart for user. userId={}", user.getId());
        Cart cart = new Cart();
        cart.setUser(user);
        Cart savedCart = cartRepository.save(cart);
        log.debug("Cart created successfully. cartId={}, userId={}", savedCart.getId(), user.getId());
        return savedCart;
    }
}

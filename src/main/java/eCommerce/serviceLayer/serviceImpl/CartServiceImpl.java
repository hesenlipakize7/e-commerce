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
        Optional<CartItem> optionalItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        if (optionalItem.isPresent()) {
            CartItem cartItem = optionalItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    @Transactional
    public void removeProductFromCart(Long productId) {
        User user = userService.getAuthenticatedUser();
        log.info("Remove product from cart requested. ");
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> {
                    log.warn("Remove product failed. Cart not found. ");
                    return new BadRequestException("Cart is empty");
                });

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> {
                    log.warn("Product not found in cart.");
                    return new NotFoundException("Product not in cart");
                });
        cartItemRepository.delete(cartItem);
    log.info("Product removed from cart successfully. ");
    }

    @Override
    @Transactional
    public void updateProductQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            log.warn("Invalid quantity update attempted.");
            throw new BadRequestException("Quantity must be greater than 0");
        }
        User user = userService.getAuthenticatedUser();
        log.info("Update product quantity requested. ");
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> {
                    log.warn("Cart not found during quantity update.");
                    return new NotFoundException("Cart not found");
                });
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> {
                    log.warn("Product not found in cart during quantity update. ");
                    return new NotFoundException("Product not in cart");
                });
        cartItem.setQuantity(quantity);
        log.debug("Product quantity updated in cart.");
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getMyCart() {
        User user = userService.getAuthenticatedUser();
        log.info("Fetching cart for userId={} ", user.getId());
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItemResponse> items = cartMapper.toItemDtoList(cartItems);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {
            Product product = item.getProduct();
            if (product == null || product.getPrice() == null) {
                log.warn("Invalid product in cart. cartItemId={} ", item.getId());
                continue;
            }
            BigDecimal itemTotal =
                    item.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));

            totalPrice = totalPrice.add(itemTotal);
        }
        CartResponse response = new CartResponse();
        response.setCartId(cart.getId());
        response.setCartItems(items);
        response.setTotalPrice(totalPrice);
        return response;
    }

    private Cart createCart(User user) {
        log.info("Creating new cart for user. ");
        Cart cart = new Cart();
        cart.setUser(user);
        Cart savedCart = cartRepository.save(cart);
        log.debug("Cart created successfully. ");
        return savedCart;
    }
}

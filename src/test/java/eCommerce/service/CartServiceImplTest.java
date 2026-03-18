package eCommerce.service;


import eCommerce.dto.response.CartItemResponse;
import eCommerce.dto.response.CartResponse;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.CartMapper;
import eCommerce.model.entity.*;
import eCommerce.repository.CartItemRepository;
import eCommerce.repository.CartRepository;
import eCommerce.repository.ProductRepository;
import eCommerce.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Product product;
    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Phone");
        product.setPrice(BigDecimal.valueOf(1000));

        cart = new Cart();
        cart.setId(1L);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setCart(cart);

        cart.setCartItems(new ArrayList<>(List.of(cartItem)));
    }

    @Test
    void addProductToCart_success() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(1L, 1L))
                .thenReturn(Optional.empty());

        cartService.addProductToCart(1L, 2);

        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void addProductToCart_productNotFound() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> cartService.addProductToCart(1L, 2));
    }

    @Test
    void removeProductFromCart_success() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(1L, 1L))
                .thenReturn(Optional.of(cartItem));

        cartService.removeProductFromCart(1L);

        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void removeProductFromCart_cartNotFound() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> cartService.removeProductFromCart(1L));
    }

    @Test
    void updateProductQuantity_success() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(1L, 1L))
                .thenReturn(Optional.of(cartItem));

        cartService.updateProductQuantity(1L, 5);

        assertEquals(5, cartItem.getQuantity());
    }

    @Test
    void updateProductQuantity_invalidQuantity() {

        assertThrows(BadRequestException.class,
                () -> cartService.updateProductQuantity(1L, 0));
    }

    @Test
    void getMyCart_success() {

        CartItemResponse itemResponse = new CartItemResponse();
        List<CartItemResponse> itemResponses = List.of(itemResponse);

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartMapper.toItemDtoList(cart.getCartItems()))
                .thenReturn(itemResponses);

        CartResponse result = cartService.getMyCart();

        assertEquals(1, result.getCartItems().size());
        assertEquals(BigDecimal.valueOf(2000), result.getTotalPrice());
    }

    @Test
    void getMyCart_cartEmpty() {

        cart.setCartItems(new ArrayList<>());

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        assertThrows(BadRequestException.class,
                () -> cartService.getMyCart());
    }
}

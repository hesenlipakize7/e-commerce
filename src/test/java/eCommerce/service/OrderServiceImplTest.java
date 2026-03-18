package eCommerce.service;

import eCommerce.dto.request.OrderCreateRequest;
import eCommerce.dto.response.OrderResponse;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.OrderMapper;
import eCommerce.model.entity.*;
import eCommerce.model.enums.OrderStatus;
import eCommerce.repository.CartRepository;
import eCommerce.repository.OrderRepository;
import eCommerce.service.impl.OrderServiceImpl;

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
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Cart cart;
    private CartItem cartItem;
    private Product product;
    private Order order;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Phone");
        product.setPrice(BigDecimal.valueOf(1000));
        product.setStock(10);

        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setId(1L);
        cart.setCartItems(new ArrayList<>(List.of(cartItem)));

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CREATED);

        orderResponse = new OrderResponse();
        orderResponse.setOrderId(1L);
    }

    @Test
    void checkout_success() {

        OrderCreateRequest request = new OrderCreateRequest();

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderService.checkout(request);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());

        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).toOrderResponse(order);
    }

    @Test
    void checkout_cartNotFound() {

        OrderCreateRequest request = new OrderCreateRequest();

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> orderService.checkout(request));
    }

    @Test
    void checkout_cartEmpty() {

        cart.setCartItems(new ArrayList<>());

        OrderCreateRequest request = new OrderCreateRequest();

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        assertThrows(BadRequestException.class,
                () -> orderService.checkout(request));
    }

    @Test
    void checkout_insufficientStock() {

        product.setStock(1);

        OrderCreateRequest request = new OrderCreateRequest();

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        assertThrows(BadRequestException.class,
                () -> orderService.checkout(request));
    }

    @Test
    void getMyOrders_success() {

        List<Order> orders = List.of(order);
        List<OrderResponse> responses = List.of(orderResponse);

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(orderRepository.findAllByUserId(1L)).thenReturn(orders);
        when(orderMapper.toOrderResponseList(orders)).thenReturn(responses);

        List<OrderResponse> result = orderService.getMyOrders();

        assertEquals(1, result.size());

        verify(orderRepository).findAllByUserId(1L);
    }

    @Test
    void getOrderDetails_success() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(orderRepository.findWithItemsById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderService.getOrderDetails(1L);

        assertEquals(1L, result.getOrderId());
    }

    @Test
    void getOrderDetails_notFound() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(orderRepository.findWithItemsById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> orderService.getOrderDetails(1L));
    }

}

package eCommerce.service;


import eCommerce.dto.request.PaymentCreateRequest;
import eCommerce.dto.response.PaymentResponse;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.PaymentMapper;
import eCommerce.model.entity.*;
import eCommerce.model.enums.OrderStatus;
import eCommerce.model.enums.PaymentMethod;
import eCommerce.model.enums.PaymentStatus;
import eCommerce.payment.factory.PaymentStrategyFactory;
import eCommerce.payment.strategy.PaymentStrategy;
import eCommerce.repository.CartRepository;
import eCommerce.repository.OrderRepository;
import eCommerce.repository.PaymentRepository;
import eCommerce.service.impl.PaymentServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    private PaymentStrategy paymentStrategy;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private Payment payment;
    private PaymentResponse paymentResponse;
    private PaymentCreateRequest request;
    private Cart cart;
    private Product product;
    private CartItem cartItem;
    private User user;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);


        payment = new Payment();
        payment.setId(1L);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setOrder(order);

        paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentId(1L);
        paymentResponse.setOrderId(1L);

        request = new PaymentCreateRequest();
        request.setOrderId(1L);
        request.setPaymentMethod(PaymentMethod.valueOf("CARD"));

        product = new Product();
        product.setStock(10);

        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setId(1L);
        cart.setCartItems(new ArrayList<>(List.of(cartItem)));
    }

    @Test
    void pay_success() {

        PaymentCreateRequest request = new PaymentCreateRequest();
        request.setOrderId(1L);
        request.setPaymentMethod(PaymentMethod.CARD);

        User user = new User();
        user.setId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CREATED);

        Product product = new Product();
        product.setStock(10);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCartItems(new ArrayList<>(List.of(cartItem))); // mutable!

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        when(orderRepository.findWithItemsById(1L))
                .thenReturn(Optional.of(order));

        when(paymentStrategyFactory.getStrategy(any()))
                .thenReturn(paymentStrategy);

        when(paymentStrategy.processPayment(order, request))
                .thenReturn(payment);

        when(paymentRepository.save(payment))
                .thenReturn(payment);

        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.of(cart));

        when(paymentMapper.toDto(payment))
                .thenReturn(new PaymentResponse());

        PaymentResponse result = paymentService.pay(request);

        assertNotNull(result);
        assertEquals(OrderStatus.PAID, order.getOrderStatus());
        assertEquals(8, product.getStock()); // 10 - 2

        verify(orderRepository).findWithItemsById(1L);
        verify(paymentRepository).save(payment);
    }

    @Test
    void pay_orderNotFound() {

        PaymentCreateRequest request = new PaymentCreateRequest();
        request.setOrderId(1L);

        when(orderRepository.findWithItemsById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> paymentService.pay(request));
    }

    @Test
    void pay_alreadyPaid() {

        PaymentCreateRequest request = new PaymentCreateRequest();
        request.setOrderId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.PAID);

        when(orderRepository.findWithItemsById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,
                () -> paymentService.pay(request));
    }

}

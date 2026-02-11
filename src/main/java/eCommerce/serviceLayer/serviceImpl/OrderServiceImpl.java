package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.request.OrderCreateRequest;
import eCommerce.dto.response.OrderResponse;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.OrderMapper;
import eCommerce.model.entity.*;
import eCommerce.model.enums.OrderStatus;
import eCommerce.repository.*;
import eCommerce.serviceLayer.service.OrderService;
import eCommerce.serviceLayer.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;


    @Override
    public OrderResponse checkout(OrderCreateRequest orderCreateRequest) {
        log.info("Checkout started");
        User user = userService.getAuthenticatedUser();
        Cart cart = getUserCart(user);
        validateStock(cart);
        Order order = createOrder(user);
        List<OrderItem> orderItems = createOrderItems(cart, order);
        order.setOrderItems(orderItems);
        BigDecimal totalAmount = calculateTotal(orderItems);
        order.setTotalAmount(totalAmount);
        decreaseStock(cart);
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully");
        clearCart(cart);
        log.info("Checkout finished successfully");
        return orderMapper.toOrderResponse(savedOrder);
    }

    private Cart getUserCart(User user) {
        log.debug("Fetching cart for userId={}", user.getId());
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> {
                    return new NotFoundException("Cart not found");
                });
        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        log.info("Cart found");
        return cart;
    }

    private Order createOrder(User user) {
        log.debug("Creating order for user");
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        log.debug("Creating order items");
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        log.info("Order items created");
        return orderItems;
    }

    private void validateStock(Cart cart) {
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("There is not enough stock");
            }
        }
    }

    private void decreaseStock(Cart cart) {
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
        }
        log.debug("Stock decreased");
    }

    private BigDecimal calculateTotal(List<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            BigDecimal itemTotal =
                    item.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }
        log.info("Total amount calculated: {}", total);
        return total;
    }

    private void clearCart(Cart cart) {
        log.info("Clearing cart id={}", cart.getId());
        cartRepository.delete(cart);
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders() {
        log.info("Fetching orders for current user");
        User user = userService.getAuthenticatedUser();
        log.debug("Authenticated user");
        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        log.info("Orders fetched");
        return orderMapper.toOrderResponseList(orders);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public OrderResponse getOrderDetails(Long orderId) {
        log.info("Fetching order details");
        User user = userService.getAuthenticatedUser();
        log.debug("Authenticated user ");
        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> {
                    return new NotFoundException("Order not found");
                });
        log.info("Order found");
        return orderMapper.toOrderResponse(order);
    }
}

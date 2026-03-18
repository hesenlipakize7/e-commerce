package eCommerce.service.impl;

import eCommerce.dto.request.OrderCreateRequest;
import eCommerce.dto.response.OrderResponse;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.OrderMapper;
import eCommerce.model.entity.*;
import eCommerce.model.enums.OrderStatus;
import eCommerce.repository.*;
import eCommerce.service.OrderService;
import eCommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;


    @Override
    @Transactional
    public OrderResponse checkout(OrderCreateRequest orderCreateRequest) {
        User user = userService.getAuthenticatedUser();
        log.info("Checkout started. userId={}",user.getId());
        Cart cart = getUserCart(user);
        validateStock(cart);
        Order order = createOrder(user);
        List<OrderItem> orderItems = createOrderItems(cart, order);
        order.setOrderItems(orderItems);
        BigDecimal totalAmount = calculateTotal(orderItems);
        order.setTotalAmount(totalAmount);
        order.setCart(cart);
        Order savedOrder = orderRepository.save(order);
        log.info("Checkout finished successfully. orderId={}", savedOrder.getId());
        return orderMapper.toOrderResponse(savedOrder);
    }

    private Cart getUserCart(User user) {
        log.info("Fetching cart for userId={}", user.getId());
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> {
                    return new NotFoundException("Cart not found");
                });
        if (cart.getCartItems().isEmpty()) {
            log.warn("Checkout failed. Cart is empty. userId={}, cartId={}", user.getId(), cart.getId());
            throw new BadRequestException("Cart is empty");
        }
        log.info("Cart found. cartId={}, userId={}, itemCount={}", cart.getId(), user.getId(), cart.getCartItems().size());
        return cart;
    }

    private Order createOrder(User user) {
        log.info("Creating order for userId={}", user.getId());
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        log.info("Creating order items. cartId={}", cart.getId());
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
            log.info("Order item created. productName={}, quantity={}", product.getName(), cartItem.getQuantity());
        }
        log.info("Order items created successfully. orderItemCount={}", orderItems.size());
        return orderItems;
    }

    private void validateStock(Cart cart) {
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            log.warn("Insufficient stock. productId={}, requested={}, available={}",product.getId(),cartItem.getQuantity(),product.getStock());
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("There is not enough stock");
            }
        }
        log.info("Stock validation passed. cartId={}", cart.getId());
    }


    private BigDecimal calculateTotal(List<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            BigDecimal itemTotal =
                    item.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }
        log.info("Total amount calculated. totalAmount={}", total);
        return total;
    }


    @Override
    @Transactional
    public List<OrderResponse> getMyOrders() {
        User user = userService.getAuthenticatedUser();
        log.info("Fetching orders for userId={}", user.getId());
        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        log.info("Orders fetched successfully. userId={}, orderCount={}", user.getId(), orders.size());
        return orderMapper.toOrderResponseList(orders);
    }

    @Override
    public OrderResponse getOrderDetails(Long orderId) {
        User user = userService.getAuthenticatedUser();
        log.info("Fetching order details. orderId={}, userId={}", orderId, user.getId());
        Order order = orderRepository.findWithItemsById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order not found. orderId={}", orderId);
                    return new NotFoundException("Order not found");
                });
        log.info("Order details fetched successfully. orderId={}", orderId);
        return orderMapper.toOrderResponse(order);
    }
}

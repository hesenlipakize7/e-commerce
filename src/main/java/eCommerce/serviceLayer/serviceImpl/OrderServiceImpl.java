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
    private final CategoryRepository categoryRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;


    @Override
    public OrderResponse checkout(OrderCreateRequest orderCreateRequest) {
        User user = userService.getAuthenticatedUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("Cart is empty"));
        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException(product.getName() + "stock is not enough");
            }
            product.setStock(product.getStock() - cartItem.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(product.getStock());

            orderItems.add(orderItem);

            totalPrice = totalPrice.add(product.getPrice())
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalPrice);
        Order savedOrder = orderRepository.save(order);
        cartRepository.delete(cart);
        return orderMapper.toOrderResponse(savedOrder);
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders() {
        User user = userService.getAuthenticatedUser();
        return orderMapper.toOrderResponseList(orderRepository.findAllByUserId(user.getId()));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public OrderResponse getOrderDetails(Long orderId) {
        User user = userService.getAuthenticatedUser();
        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(()-> new NotFoundException("Order not found"));
        return orderMapper.toOrderResponse(order);
    }
}

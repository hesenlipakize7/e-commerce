package eCommerce.service.impl;

import eCommerce.dto.request.PaymentCreateRequest;
import eCommerce.dto.response.PaymentResponse;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.PaymentMapper;
import eCommerce.model.entity.*;
import eCommerce.model.enums.OrderStatus;
import eCommerce.model.enums.PaymentStatus;
import eCommerce.payment.factory.PaymentStrategyFactory;
import eCommerce.payment.strategy.PaymentStrategy;
import eCommerce.repository.*;
import eCommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentMapper paymentMapper;


    @Override
    public PaymentResponse pay(PaymentCreateRequest createRequest) {
        log.info("Payment started. orderId={}, paymentMethod={}", createRequest.getOrderId(), createRequest.getPaymentMethod());
        Order order = getOrder(createRequest.getOrderId());
        validateOrderPayment(order);

        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(createRequest.getPaymentMethod());
        log.info("Payment strategy selected. method={}", strategy.getClass().getName());

        Payment payment = strategy.processPayment(order, createRequest);
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment processed. paymentId={}, orderId={}, status={}", savedPayment, order.getId(), savedPayment.getPaymentStatus());

        order.setOrderStatus(OrderStatus.PAID);
        order.setPayment(savedPayment);

        if (savedPayment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            Cart cart = cartRepository.findByUserId(order.getUser().getId())
                    .orElseThrow(() -> new NotFoundException("Cart not found"));
            for (CartItem cartItem : cart.getCartItems()) {
                Product product = cartItem.getProduct();
                product.setStock(product.getStock() - cartItem.getQuantity());
            }
            cart.getCartItems().clear();
            log.info("Cart cleared after successful payment. cartId={}", cart.getId());
        }

        log.info("Payment flow finished. orderId={}, paymentId={}", order.getId(), savedPayment.getId());

        return paymentMapper.toDto(savedPayment);
    }

    private Order getOrder(Long orderId) {
        log.info("Fetching order for payment. orderId={}", orderId);
        return orderRepository.findWithItemsById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order not found during payment. orderId={}", orderId);
                    return new NotFoundException("Order not found");
                });
    }

    private void validateOrderPayment(Order order) {
        if (order.getOrderStatus() == OrderStatus.PAID) {
            log.warn("Payment attempt for already paid order. orderId={}", order.getId());
            throw new BadRequestException("Order is already paid");
        }
        log.info("Order payment validation passed. orderId={}", order.getId());
    }
}

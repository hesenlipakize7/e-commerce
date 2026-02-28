package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.request.PaymentCreateRequest;
import eCommerce.dto.response.PaymentResponse;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.PaymentMapper;
import eCommerce.model.entity.Order;
import eCommerce.model.entity.Payment;
import eCommerce.model.enums.OrderStatus;
import eCommerce.payment.factory.PaymentStrategyFactory;
import eCommerce.payment.strategi.PaymentStrategy;
import eCommerce.repository.OrderRepository;
import eCommerce.repository.PaymentRepository;
import eCommerce.serviceLayer.service.PaymentService;
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
    private final PaymentRepository paymentRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentMapper paymentMapper;


    @Override
    public PaymentResponse pay(PaymentCreateRequest createRequest) {
        log.info("Payment started");
        Order order = getOrder(createRequest.getOrderId());
        validateOrderPayment(order);

        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(createRequest.getPaymentMethod());

        Payment payment = strategy.processPayment(order, createRequest);
        Payment savedPayment = paymentRepository.save(payment);

        order.setOrderStatus(OrderStatus.PAID);
        order.setPayment(savedPayment);

        log.info("Payment successful");

        return paymentMapper.toDto(savedPayment);
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order not found");
                    return new NotFoundException("Order not found");
                });
    }

    private void validateOrderPayment(Order order) {
        if (order.getOrderStatus() == OrderStatus.PAID) {
            log.warn("Order already paid.");
            throw new BadRequestException("Order is already paid");
        }
    }
}

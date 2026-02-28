package eCommerce.payment.strategi;

import eCommerce.dto.request.PaymentCreateRequest;
import eCommerce.model.entity.Order;
import eCommerce.model.entity.Payment;
import eCommerce.model.enums.PaymentMethod;
import eCommerce.model.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CARD;
    }

    @Override
    public Payment processPayment(Order order, PaymentCreateRequest createRequest) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(createRequest.getPaymentMethod());
        payment.setAmount(order.getTotalAmount());


        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentDate(LocalDateTime.now());

        return payment;
    }
}

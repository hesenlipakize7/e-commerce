package eCommerce.payment.strategi;

import eCommerce.dto.request.PaymentCreateRequest;
import eCommerce.model.entity.Order;
import eCommerce.model.entity.Payment;
import eCommerce.model.enums.PaymentMethod;
import eCommerce.model.enums.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class CashOnDeliveryPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CASH_ON_DELIVERY;
    }

    @Override
    public Payment processPayment(Order order, PaymentCreateRequest createRequest) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(createRequest.getPaymentMethod());
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        return payment;
    }
}

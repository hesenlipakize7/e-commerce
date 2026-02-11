package eCommerce.payment.strategi;

import eCommerce.dto.request.PaymentCreateRequest;
import eCommerce.model.entity.Order;
import eCommerce.model.entity.Payment;
import eCommerce.model.enums.PaymentMethod;

public interface PaymentStrategy {
    Payment processPayment(Order order, PaymentCreateRequest createRequest);
    PaymentMethod getPaymentMethod();
}

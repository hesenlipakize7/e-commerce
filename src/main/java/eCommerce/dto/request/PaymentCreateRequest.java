package eCommerce.dto.request;

import eCommerce.model.enums.PaymentMethod;
import lombok.Data;

@Data
public class PaymentCreateRequest {
    private Long orderId;

    private PaymentMethod paymentMethod;

    private String cardNumber;
    private String cardHolderName;
}

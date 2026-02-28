package eCommerce.dto.request;

import eCommerce.model.enums.PaymentMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentCreateRequest {
    private Long orderId;

    private PaymentMethod paymentMethod;

    private String cardNumber;

    private String cardHolderName;
}

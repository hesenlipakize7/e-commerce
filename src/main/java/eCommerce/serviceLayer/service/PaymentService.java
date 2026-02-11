package eCommerce.serviceLayer.service;

import eCommerce.dto.request.PaymentCreateRequest;
import eCommerce.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse pay(PaymentCreateRequest createRequest);
}

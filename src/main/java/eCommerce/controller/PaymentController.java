package eCommerce.controller;

import eCommerce.dto.request.PaymentCreateRequest;
import eCommerce.dto.response.PaymentResponse;
import eCommerce.serviceLayer.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@PreAuthorize("hasRole('USER')")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping()
    public ResponseEntity<PaymentResponse> pay(@Valid @RequestBody PaymentCreateRequest paymentCreateRequest) {
        PaymentResponse paymentResponse = paymentService.pay(paymentCreateRequest);
        return ResponseEntity.ok(paymentResponse);
    }
}

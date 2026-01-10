package eCommerce.controller;

import eCommerce.dto.request.OrderCreateRequest;
import eCommerce.dto.response.OrderResponse;
import eCommerce.serviceLayer.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/checkout")
    public OrderResponse checkout(@RequestBody @Valid OrderCreateRequest orderCreateRequest) {
        return orderService.checkout(orderCreateRequest);
    }

    @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders() {
        return orderService.getMyOrders();
    }

    @GetMapping("{orderId}")
    public OrderResponse getOrderDetails(@PathVariable Long orderId) {
        return orderService.getOrderDetails(orderId);
    }
}

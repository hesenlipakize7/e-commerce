package eCommerce.serviceLayer.service;

import eCommerce.dto.request.OrderCreateRequest;
import eCommerce.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse checkout(OrderCreateRequest orderCreateRequest);
    List<OrderResponse> getMyOrders();
    OrderResponse getOrderDetails(Long orderId);
}

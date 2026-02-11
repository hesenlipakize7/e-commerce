package eCommerce.mapper;

import eCommerce.dto.response.OrderItemResponse;
import eCommerce.dto.response.OrderResponse;
import eCommerce.model.entity.Order;
import eCommerce.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "orderId", source = "order.id")
    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toOrderResponseList(List<Order> orders);

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}

package eCommerce.mapper;

import eCommerce.dto.response.OrderItemResponse;
import eCommerce.dto.response.OrderResponse;
import eCommerce.entity.Order;
import eCommerce.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}

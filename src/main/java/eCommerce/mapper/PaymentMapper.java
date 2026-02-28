package eCommerce.mapper;

import eCommerce.dto.response.PaymentResponse;
import eCommerce.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "order.id",target = "orderId")
    @Mapping(source = "id", target = "paymentId")
    PaymentResponse toDto(Payment payment);
}

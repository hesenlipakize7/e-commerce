package eCommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

public class OrderCreateRequest {
    @NotNull(message = "AddressId boş ola bilməz")
    private Long addressId;
}

package eCommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class OrderCreateRequest {
    @NotNull(message = "Çatdırılma ünvanı seçilməlidir")
    private Long addressId;
}

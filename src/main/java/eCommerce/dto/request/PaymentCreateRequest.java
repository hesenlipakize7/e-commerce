package eCommerce.dto.request;

import eCommerce.model.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PaymentCreateRequest {
    @NotNull(message = "Sifariş seçilməlidir")
    private Long orderId;

    @NotNull(message = "Ödəniş metodu seçilməlidir")
    private PaymentMethod paymentMethod;

    @NotBlank(message = "Kart nömrəsi boş ola bilməz")
    @Pattern(regexp = "\\d{16}",message = "Kart nömrəsi 16 rəqəmdən ibarət olmalıdır")
    private String cardNumber;

    @NotBlank(message = "Kart sahibinin adı boş ola bilməz")
    private String cardHolderName;
}

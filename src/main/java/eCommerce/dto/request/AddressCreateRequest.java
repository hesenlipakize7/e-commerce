package eCommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AddressCreateRequest {
    @NotBlank(message = "Şəhər boş ola bilməz")
    private String city;

    @NotBlank(message = "Rayon boş ola bilməz")
    private String district;

    @NotBlank(message = "Küçə boş ola bilməz")
    private String street;

    @NotBlank(message = "poçt kodu boş ola bilməz")
    private String postalCode;

    @NotBlank(message = "Telefon nömrəsi boş ola bilməz")
    private String phoneNumber;
}

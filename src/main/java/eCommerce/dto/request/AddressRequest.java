package eCommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AddressRequest {
    @NotBlank(message = "city boş ola bilməz")
    private String city;

    @NotBlank(message = "district boş ola bilməz")
    private String district;

    @NotBlank(message = "street boş ola bilməz")
    private String street;

    @NotBlank(message = "postalCode boş ola bilməz")
    private String postalCode;
}

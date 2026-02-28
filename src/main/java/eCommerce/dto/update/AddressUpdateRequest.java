package eCommerce.dto.update;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressUpdateRequest {
    private String city;
    private String district;
    private String street;
    private String phoneNumber;
    private String postalCode;
}

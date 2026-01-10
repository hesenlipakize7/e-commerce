package eCommerce.dto.update;

import lombok.Data;

@Data
public class AddressUpdateRequest {
    private String city;
    private String district;
    private String street;
    private String phoneNumber;
    private String postalCode;
}

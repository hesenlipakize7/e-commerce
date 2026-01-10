package eCommerce.dto.response;

import lombok.*;


@Data
public class AddressResponse {
    private Long id;
    private String city;
    private String district;
    private String street;
    private String postalCode;
    private String phoneNumber;
}

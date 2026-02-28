package eCommerce.dto.response;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class AddressResponse {
    private Long id;
    private String city;
    private String district;
    private String street;
    private String postalCode;
    private String phoneNumber;
}

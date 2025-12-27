package eCommerce.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class AddressResponse {
    private Long id;
    private String city;
    private String district;
    private String street;
    private String postalCode;
}

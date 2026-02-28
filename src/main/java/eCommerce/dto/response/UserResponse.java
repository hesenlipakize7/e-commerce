package eCommerce.dto.response;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String surname;
    private String email;
}

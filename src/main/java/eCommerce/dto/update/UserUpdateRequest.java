package eCommerce.dto.update;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
}

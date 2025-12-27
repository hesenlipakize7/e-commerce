package eCommerce.dto.update;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserUpdateRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
}

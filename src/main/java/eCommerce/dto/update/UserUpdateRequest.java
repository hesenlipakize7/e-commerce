package eCommerce.dto.update;

import lombok.Data;


@Data
public class UserUpdateRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
}

package eCommerce.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserLoginRequest {
    private String email;
    private String password;
}

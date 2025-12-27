package eCommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class UserRegisterRequest {
    @NotBlank(message = "Name boş ola bilməz")
    private String name;

    @NotBlank(message = "surname boş ola bilməz")
    private String surname;

    @NotBlank(message = "email boş ola bilməz")
    private String email;

    @Size(min = 7, message = "şifrə minimum 7 simvol olmalıdır")
    private String password;

    @NotNull(message = "nomre bos qala bilmez")
    private Long phone;
}

package eCommerce.dto.response;

import lombok.*;


@Data
public class UserResponse {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private Long phone;

    private int favoriteCount;
}

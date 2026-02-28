package eCommerce.serviceLayer.service;

import eCommerce.dto.response.UserResponse;
import eCommerce.dto.update.ChangePasswordRequest;
import eCommerce.dto.update.UserUpdateRequest;
import eCommerce.model.entity.User;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    User getAuthenticatedUser();

    UserResponse getMyProfile();

    UserResponse updateMyProfile(UserUpdateRequest userUpdateRequest);

    UserResponse changeMyPassword(ChangePasswordRequest changePasswordRequest);

}

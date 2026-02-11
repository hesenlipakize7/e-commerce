package eCommerce.controller;

import eCommerce.dto.response.UserResponse;
import eCommerce.dto.update.ChangePasswordRequest;
import eCommerce.dto.update.UserUpdateRequest;
import eCommerce.serviceLayer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/me")
    public UserResponse getMyProfile() {
        return userService.getMyProfile();
    }

    @PutMapping("/me/update-profile")
    public UserResponse updateMyProfile(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.updateMyProfile(userUpdateRequest);
    }

    @PutMapping("/me/change-password")
    public ResponseEntity<Void> changeMyPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changeMyPassword(changePasswordRequest);
        return ResponseEntity.ok().build();
    }
}

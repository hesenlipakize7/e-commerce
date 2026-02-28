package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.update.ChangePasswordRequest;
import eCommerce.dto.update.UserUpdateRequest;
import eCommerce.dto.response.UserResponse;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.exception.UnauthorizedException;
import eCommerce.model.entity.User;
import eCommerce.mapper.UserMapper;
import eCommerce.repository.UserRepository;
import eCommerce.serviceLayer.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            log.warn("Unauthorized access attempt");
            throw new UnauthorizedException("User is not authenticated");
        }
        String email = authentication.getName();
        log.debug("Authenticated user email extracted from token.");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                log.error("Authenticated user not found in database.");
                        return new NotFoundException("User not found");
                });
    }

    @Override
    public UserResponse getMyProfile() {
        User user= getAuthenticatedUser();
        log.info("User profile requested.");
        return userMapper.toDto(user);
    }


    @Override
    public UserResponse updateMyProfile(UserUpdateRequest userUpdateRequest) {
        User user = getAuthenticatedUser();
        log.info("User profile update started.");
        userMapper.updateUserFromDto(userUpdateRequest, user);
        userRepository.save(user);
        log.info("User profile updated successfully.");
        return userMapper.toDto(user);
    }

    @Override
    public UserResponse changeMyPassword(ChangePasswordRequest changePasswordRequest) {
        User user = getAuthenticatedUser();
        log.info("Password change attempt. userId={}", user.getId());
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            log.warn("Password change failed. Wrong current password.");
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed successfully.");
        return  userMapper.toDto(user);
    }
}

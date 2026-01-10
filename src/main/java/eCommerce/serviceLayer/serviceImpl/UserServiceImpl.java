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
            throw new UnauthorizedException("User is not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserResponse getMyProfile() {
        return userMapper.toDto(getAuthenticatedUser());
    }


    @Override
    public UserResponse updateMyProfile(UserUpdateRequest userUpdateRequest) {
        User user = getAuthenticatedUser();
        userMapper.updateUserFromDto(userUpdateRequest, user);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public void changeMyPassword(ChangePasswordRequest changePasswordRequest) {
        User user = getAuthenticatedUser();
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }
}

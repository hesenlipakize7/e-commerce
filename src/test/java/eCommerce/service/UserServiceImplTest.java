package eCommerce.service;


import eCommerce.dto.response.UserResponse;
import eCommerce.dto.update.ChangePasswordRequest;
import eCommerce.dto.update.UserUpdateRequest;
import eCommerce.exception.BadRequestException;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.UserMapper;
import eCommerce.model.entity.User;
import eCommerce.repository.UserRepository;
import eCommerce.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setPassword("encodedPassword");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("test@mail.com");

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "test@mail.com",
                        null,
                        new ArrayList<>()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getAuthenticatedUser_success() {

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        User result = userService.getAuthenticatedUser();

        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    void getAuthenticatedUser_userNotFound() {

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getAuthenticatedUser());
    }

    @Test
    void getMyProfile_success() {

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponse);

        UserResponse result = userService.getMyProfile();

        assertEquals(1L, result.getId());
        verify(userMapper).toDto(user);
    }

    @Test
    void updateMyProfile_success() {

        UserUpdateRequest request = new UserUpdateRequest();

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponse);

        UserResponse result = userService.updateMyProfile(request);

        verify(userMapper).updateUserFromDto(request, user);
        verify(userRepository).save(user);

        assertNotNull(result);
    }

    @Test
    void changeMyPassword_success() {

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("old");
        request.setNewPassword("new");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "encodedPassword"))
                .thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("newEncoded");
        when(userMapper.toDto(user)).thenReturn(userResponse);

        UserResponse result = userService.changeMyPassword(request);

        verify(userRepository).save(user);
        assertNotNull(result);
    }

    @Test
    void changeMyPassword_wrongOldPassword() {

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("wrong");
        request.setNewPassword("new");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPassword"))
                .thenReturn(false);

        assertThrows(BadRequestException.class,
                () -> userService.changeMyPassword(request));
    }
}

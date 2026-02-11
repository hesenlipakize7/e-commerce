package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.auth.LoginRequest;
import eCommerce.dto.auth.RegisterRequest;
import eCommerce.dto.auth.RegisterResponse;
import eCommerce.exception.AlreadyExistsException;
import eCommerce.mapper.UserMapper;
import eCommerce.model.entity.User;
import eCommerce.repository.UserRepository;
import eCommerce.security.JwtService;
import eCommerce.serviceLayer.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;


    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        log.info("User registration attempt");
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AlreadyExistsException("Email Already Exists");
        }
        User user = userMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
        return new RegisterResponse("User successfully registered");
    }

    @Override
    public String loginUser(LoginRequest loginRequest) {
        log.info("Login attempt. email={}", loginRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()));
        log.info("Login successful. email={}", loginRequest.getEmail());
        return  jwtService.generateToken(authentication.getName());
    }
}

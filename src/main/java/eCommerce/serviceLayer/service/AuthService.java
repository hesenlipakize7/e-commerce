package eCommerce.serviceLayer.service;

import eCommerce.dto.auth.LoginRequest;
import eCommerce.dto.auth.RegisterRequest;
import eCommerce.dto.auth.RegisterResponse;

public interface AuthService {
    RegisterResponse registerUser(RegisterRequest registerRequest);

    String loginUser(LoginRequest loginRequest);
}

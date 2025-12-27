package eCommerce.serviceLayer.service;

import eCommerce.dto.request.UserRegisterRequest;
import eCommerce.dto.update.UserUpdateRequest;
import eCommerce.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserResponse registerUser(UserRegisterRequest request);
    UserResponse getById(Long id);
    List<UserResponse> getAll();
    UserResponse updateUser(UserUpdateRequest request,Long id);
    void deleteUser(Long id);
}

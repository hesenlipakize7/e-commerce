package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.request.UserRegisterRequest;
import eCommerce.dto.update.UserUpdateRequest;
import eCommerce.dto.response.UserResponse;
import eCommerce.entity.User;
import eCommerce.mapper.UserMapper;
import eCommerce.repository.UserRepository;
import eCommerce.serviceLayer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email artıq istifadə olunub");
        }
        User user = userMapper.toEntity(request);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tapılmadı"));

        return userMapper.toDto(user);
    }

    @Override
    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toDto(users);
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tapılmadı"));
        if (request.getName() != null)
            user.setName(request.getName());

        if (request.getSurname() != null)
            user.setSurname(request.getSurname());

        if (request.getPassword() != null)
            user.setPassword(request.getPassword());

        if (request.getPhone() != null)
            user.setPhone(Long.valueOf(request.getPhone()));

        if (request.getEmail() != null) {
            if(userRepository.existsByEmail(request.getEmail()))
                throw new RuntimeException("Bu email artıq istifadə olunur");

            user.setEmail(request.getEmail());
        }
        userRepository.save(user);
        return  userMapper.toDto(user);

    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)){
            throw new RuntimeException("User tapılmadı");
        }
        userRepository.deleteById(id);
    }
}

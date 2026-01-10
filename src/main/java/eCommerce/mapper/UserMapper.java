package eCommerce.mapper;

import eCommerce.dto.auth.RegisterRequest;
import eCommerce.dto.request.UserRegisterRequest;
import eCommerce.dto.update.UserUpdateRequest;
import eCommerce.dto.response.UserResponse;
import eCommerce.model.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    User toEntity(UserRegisterRequest userRegisterRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    User toEntity(RegisterRequest registerRequest);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserResponse updateUserFromDto(UserUpdateRequest request, @MappingTarget User user);

    @Mapping(target = "favoriteCount", expression = "java(user.getFavorites().size())")
    UserResponse toDto(User user);

    List<UserResponse> toDto(List<User> users);
}

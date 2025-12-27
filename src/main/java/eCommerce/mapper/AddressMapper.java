package eCommerce.mapper;

import eCommerce.dto.request.AddressRequest;
import eCommerce.dto.response.AddressResponse;
import eCommerce.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toEntity (AddressRequest addressRequest);

    AddressResponse toDto(Address address);
}

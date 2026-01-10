package eCommerce.mapper;

import eCommerce.dto.request.AddressCreateRequest;
import eCommerce.dto.response.AddressResponse;
import eCommerce.dto.update.AddressUpdateRequest;
import eCommerce.model.entity.Address;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "user",  ignore = true)
    Address toEntity(AddressCreateRequest addressCreateRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromDto(AddressUpdateRequest addressUpdateRequest, @MappingTarget Address address);

    AddressResponse toDto(Address address);

    List<AddressResponse> toResponseList (List<Address> addressList);
}

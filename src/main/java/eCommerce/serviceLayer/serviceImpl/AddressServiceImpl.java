package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.request.AddressCreateRequest;
import eCommerce.dto.response.AddressResponse;
import eCommerce.dto.update.AddressUpdateRequest;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.AddressMapper;
import eCommerce.model.entity.Address;
import eCommerce.model.entity.User;
import eCommerce.repository.AddressRepository;
import eCommerce.serviceLayer.service.AddressService;
import eCommerce.serviceLayer.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserService userService;

    @Override
    public AddressResponse createAddress(AddressCreateRequest addressCreateRequest) {
        User user = userService.getAuthenticatedUser();

        Address address = addressMapper.toEntity(addressCreateRequest);
        address.setUser(user);
        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<AddressResponse> getMyAddresses() {
        User user = userService.getAuthenticatedUser();
        return addressMapper.toResponseList(addressRepository.findAllByUserId(user.getId()));
    }

    @Override
    public AddressResponse updateAddress(Long addressId, AddressUpdateRequest addressUpdateRequest) {
        User user = userService.getAuthenticatedUser();
        Address address = addressRepository.findByIdAndUserId(addressId, user.getId())
                .orElseThrow(() -> new NotFoundException("Address not found"));
        addressMapper.updateAddressFromDto(addressUpdateRequest, address);
        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long addressId) {
        User user = userService.getAuthenticatedUser();
        Address address = addressRepository.findByIdAndUserId(addressId, user.getId())
                .orElseThrow(() -> new NotFoundException("Address not found"));
        addressRepository.delete(address);
    }
}

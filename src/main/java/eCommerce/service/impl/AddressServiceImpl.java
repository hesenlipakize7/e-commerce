package eCommerce.service.impl;

import eCommerce.dto.request.AddressCreateRequest;
import eCommerce.dto.response.AddressResponse;
import eCommerce.dto.update.AddressUpdateRequest;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.AddressMapper;
import eCommerce.model.entity.Address;
import eCommerce.model.entity.User;
import eCommerce.repository.AddressRepository;
import eCommerce.service.AddressService;
import eCommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserService userService;

    @Override
    public AddressResponse createAddress(AddressCreateRequest addressCreateRequest) {
        User user = userService.getAuthenticatedUser();
        log.info("Create address request received for userId={} ", user.getId());
        Address address = addressMapper.toEntity(addressCreateRequest);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        log.info("Address created successfully.addressId={}, userId={} ", savedAddress.getId(), user.getId());
        return addressMapper.toDto(savedAddress);
    }

    @Override
    public List<AddressResponse> getMyAddresses() {
        User user = userService.getAuthenticatedUser();
        log.info("Fetching addresses for userId={} ", user.getId());
        List<Address> addresses = addressRepository.findAllByUserId(user.getId());
        List<AddressResponse> addressResponseList = addressMapper.toResponseList(addresses);
        log.info("Fetched {} addresses for userId={}", addressResponseList.size(), user.getId());
        return addressResponseList;
    }

    @Override
    public AddressResponse updateAddress(Long addressId, AddressUpdateRequest addressUpdateRequest) {
        User user = userService.getAuthenticatedUser();
        log.info("Update address request received. addressId={}, userId={}", addressId, user.getId());
        Address address = addressRepository.findByIdAndUserId(addressId, user.getId())
                .orElseThrow(() -> {
                    log.warn("Address not found for update. addressId={}, userId={}", addressId, user.getId());
                    return new NotFoundException("Address not found");
                });
        addressMapper.updateAddressFromDto(addressUpdateRequest, address);
        Address updatedAddress = addressRepository.save(address);
        log.info("Address updated successfully. addressId={}, userId={}", addressId, user.getId());
        return addressMapper.toDto(updatedAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {
        User user = userService.getAuthenticatedUser();
        log.info("Delete address request received. addressId={}, userId={}", addressId, user.getId());
        Address address = addressRepository.findByIdAndUserId(addressId, user.getId())
                .orElseThrow(() -> {
                    log.warn("Address not found for delete. addressId={}, userId={}", addressId, user.getId());
                    return new NotFoundException("Address not found");
                });
        addressRepository.delete(address);
        log.info("Address delete successfully. addressId={}, userId={} ", addressId, user.getId());
    }
}

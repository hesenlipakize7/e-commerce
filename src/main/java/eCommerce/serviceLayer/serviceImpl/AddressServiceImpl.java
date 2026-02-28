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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserService userService;

    @Override
    public AddressResponse createAddress(AddressCreateRequest addressCreateRequest) {
        User user = userService.getAuthenticatedUser();
        log.info("Create address requested. ");
        Address address = addressMapper.toEntity(addressCreateRequest);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        log.info("Address created successfully : " + address);
        return addressMapper.toDto(savedAddress);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<AddressResponse> getMyAddresses() {
        User user = userService.getAuthenticatedUser();
        log.info("Fetching addresses for user. " );
        List<Address> addresses = addressRepository.findAllByUserId(user.getId());
        log.debug("Address count for user " );
        return addressMapper.toResponseList(addresses);
    }

    @Override
    public AddressResponse updateAddress(Long addressId, AddressUpdateRequest addressUpdateRequest) {
        User user = userService.getAuthenticatedUser();
        log.info("Update address requested. ");
        Address address = addressRepository.findByIdAndUserId(addressId, user.getId())
                .orElseThrow(() -> {
                    log.warn("Address not found for update. ");
                    return new NotFoundException("Address not found");
                });
        addressMapper.updateAddressFromDto(addressUpdateRequest, address);
        Address updatedAddress = addressRepository.save(address);
        log.info("Address updated successfully. " );
        return addressMapper.toDto(updatedAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {
        User user = userService.getAuthenticatedUser();
        log.info("Delete address requested.");
        Address address = addressRepository.findByIdAndUserId(addressId, user.getId())
                .orElseThrow(() -> {
                    log.warn("Address not found for delete. ");
                    return new NotFoundException("Address not found");
                });
        addressRepository.delete(address);
        log.info("Address delete successfully. ");
    }
}

package eCommerce.service;


import eCommerce.dto.request.AddressCreateRequest;
import eCommerce.dto.response.AddressResponse;
import eCommerce.dto.update.AddressUpdateRequest;
import eCommerce.exception.NotFoundException;
import eCommerce.mapper.AddressMapper;
import eCommerce.model.entity.Address;
import eCommerce.model.entity.User;
import eCommerce.repository.AddressRepository;
import eCommerce.service.impl.AddressServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private AddressServiceImpl addressService;

    private User user;
    private Address address;
    private AddressResponse addressResponse;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);

        address = new Address();
        address.setId(1L);

        addressResponse = new AddressResponse();
        addressResponse.setId(1L);
    }

    @Test
    void createAddress_success() {

        AddressCreateRequest request = new AddressCreateRequest();

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(addressMapper.toEntity(request)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toDto(address)).thenReturn(addressResponse);

        AddressResponse result = addressService.createAddress(request);

        assertNotNull(result);
        verify(addressRepository).save(address);
    }

    @Test
    void getMyAddresses_success() {

        List<Address> addresses = List.of(address);
        List<AddressResponse> responses = List.of(addressResponse);

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(addressRepository.findAllByUserId(1L)).thenReturn(addresses);
        when(addressMapper.toResponseList(addresses)).thenReturn(responses);

        List<AddressResponse> result = addressService.getMyAddresses();

        assertEquals(1, result.size());
        verify(addressRepository).findAllByUserId(1L);
    }

    @Test
    void updateAddress_success() {

        AddressUpdateRequest request = new AddressUpdateRequest();

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(addressRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(address));
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toDto(address)).thenReturn(addressResponse);

        AddressResponse result = addressService.updateAddress(1L, request);

        assertNotNull(result);
        verify(addressMapper).updateAddressFromDto(request, address);
        verify(addressRepository).save(address);
    }

    @Test
    void updateAddress_notFound() {

        AddressUpdateRequest request = new AddressUpdateRequest();

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(addressRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> addressService.updateAddress(1L, request));
    }

    @Test
    void deleteAddress_success() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(addressRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(address));

        addressService.deleteAddress(1L);

        verify(addressRepository).delete(address);
    }

    @Test
    void deleteAddress_notFound() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(addressRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> addressService.deleteAddress(1L));
    }
}

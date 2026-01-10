package eCommerce.serviceLayer.service;

import eCommerce.dto.request.AddressCreateRequest;
import eCommerce.dto.response.AddressResponse;
import eCommerce.dto.update.AddressUpdateRequest;
import eCommerce.model.entity.Address;

import java.util.List;

public interface AddressService {
    AddressResponse createAddress(AddressCreateRequest addressCreateRequest);
    List<AddressResponse> getMyAddresses();
    AddressResponse updateAddress(Long addressId, AddressUpdateRequest addressUpdateRequest);
    void deleteAddress(Long addressId);
}

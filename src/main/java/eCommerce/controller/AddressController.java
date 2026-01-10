package eCommerce.controller;

import eCommerce.dto.request.AddressCreateRequest;
import eCommerce.dto.response.AddressResponse;
import eCommerce.dto.update.AddressUpdateRequest;
import eCommerce.serviceLayer.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;


    @PostMapping
    public AddressResponse createAddress(@RequestBody @Valid AddressCreateRequest addressCreateRequest) {
        return addressService.createAddress(addressCreateRequest);
    }

    @GetMapping
    public List<AddressResponse> getMyAddresses() {
        return addressService.getMyAddresses();
    }

    @PutMapping("/{addressId}")
    public AddressResponse updateAddress(@PathVariable Long addressId,
                                         @RequestBody @Valid AddressUpdateRequest addressUpdateRequest) {
        return addressService.updateAddress(addressId, addressUpdateRequest);
    }

    @DeleteMapping("/{addressId}")
    public void deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
    }

}

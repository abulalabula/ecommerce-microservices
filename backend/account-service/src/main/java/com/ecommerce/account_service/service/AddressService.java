package com.ecommerce.account_service.service;

import com.ecommerce.account_service.payload.AddressDTO;
import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO);
    List<AddressDTO> getAllAddresses();
    AddressDTO getAddressById(Long id);
    AddressDTO updateAddress(Long id, AddressDTO addressDTO);
    void deleteAddress(Long id);
}

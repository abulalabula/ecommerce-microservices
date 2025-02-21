package com.ecommerce.account_service.service.impl;

import com.ecommerce.account_service.dao.AddressRepository;
import com.ecommerce.account_service.entity.Address;
import com.ecommerce.account_service.exception.DuplicateResourceException;
import com.ecommerce.account_service.exception.ResourceNotFoundException;
import com.ecommerce.account_service.payload.AddressDTO;
import com.ecommerce.account_service.service.AddressService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {

        boolean addressExists = addressRepository.existsByStreetAndCityAndStateAndZipCode(
                addressDTO.getStreet(), addressDTO.getCity(), addressDTO.getState(), addressDTO.getZipCode());

        if (addressExists) {
            throw new DuplicateResourceException("Address already exists at " + addressDTO.getStreet());
        }

        Address address = new Address(
                null, // ID auto-generated
                addressDTO.getStreet(),
                addressDTO.getCity(),
                addressDTO.getState(),
                addressDTO.getZipCode()
        );

        Address savedAddress = addressRepository.save(address);
        return convertToDTO(savedAddress);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        return address.map(this::convertToDTO).orElse(null);
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Optional<Address> existingAddress = addressRepository.findById(id);
        if (existingAddress.isPresent()) {
            Address address = existingAddress.get();
            address.setStreet(addressDTO.getStreet());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setZipCode(addressDTO.getZipCode());

            Address updatedAddress = addressRepository.save(address);
            return convertToDTO(updatedAddress);
        }
        return null;
    }

    @Override
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Address with ID: " + id + " not found.");
        }
        addressRepository.deleteById(id);
    }

    private AddressDTO convertToDTO(Address address) {
        return new AddressDTO(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZipCode()
        );
    }
}

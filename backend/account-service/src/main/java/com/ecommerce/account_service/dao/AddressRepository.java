package com.ecommerce.account_service.dao;

import com.ecommerce.account_service.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // Find address by ID
    Optional<Address> findById(Long id);

    //if an address exists already
    boolean existsByStreetAndCityAndStateAndZipCode(String street, String city, String state, String zipCode);
}

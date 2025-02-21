package com.ecommerce.account_service.dao;

import com.ecommerce.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // for authentication
    Optional<Account> findByUserEmail(String email);

    // login
    Optional<Account> findByUserName(String userName);

    // email is already registered?
    Boolean existsByUserEmail(String email);

    // username is already used?
    Boolean existsByUserName(String userName);

    // for deletion and updates
//    boolean existsById(Long id);
}

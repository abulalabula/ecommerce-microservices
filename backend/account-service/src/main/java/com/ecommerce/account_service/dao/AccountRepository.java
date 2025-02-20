package com.ecommerce.account_service.dao;

import com.ecommerce.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
//    Optional<Account> findByUserEmail(String userEmail);
}

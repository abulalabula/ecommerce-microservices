package com.ecommerce.account_service.dao;

import com.ecommerce.account_service.entity.Role;
import com.ecommerce.account_service.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // FILTER role by enum ITEM
    Optional<Role> findByName(UserRole name);
}

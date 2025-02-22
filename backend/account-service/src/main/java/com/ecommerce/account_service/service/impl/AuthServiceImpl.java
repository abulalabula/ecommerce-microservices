package com.ecommerce.account_service.service.impl;

import com.ecommerce.account_service.dao.AccountRepository;
import com.ecommerce.account_service.dao.RoleRepository;
import com.ecommerce.account_service.entity.Account;
import com.ecommerce.account_service.entity.Role;
import com.ecommerce.account_service.entity.UserRole;
import com.ecommerce.account_service.payload.security.LoginRequest;
import com.ecommerce.account_service.payload.security.SignupRequest;
import com.ecommerce.account_service.payload.security.AuthResponse;
import com.ecommerce.account_service.service.AuthService;
import com.ecommerce.account_service.service.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void registerUser(SignupRequest signupRequest) {
        if (accountRepository.existsByUserEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email is already taken.");
        }

        if (accountRepository.existsByUserName(signupRequest.getUserName())) {
            throw new RuntimeException("Username is already taken.");
        }

        Account account = new Account();
        account.setUserEmail(signupRequest.getEmail());
        account.setUserName(signupRequest.getUserName());
        account.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        if (signupRequest.getRoles() == null || signupRequest.getRoles().isEmpty()) {
            roles.add(roleRepository.findByName(UserRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Default role not found.")));
        } else {
            for (String role : signupRequest.getRoles()) {
                UserRole userRole = UserRole.valueOf(role);
                roles.add(roleRepository.findByName(userRole)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + role)));
            }
        }
        account.setRoles(roles);

        accountRepository.save(account);
    }

    @Override
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()));


        String token = jwtUtil.generateToken(authentication);


        return new AuthResponse(token);
    }
}

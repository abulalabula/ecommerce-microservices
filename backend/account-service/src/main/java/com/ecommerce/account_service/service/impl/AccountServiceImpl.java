package com.ecommerce.account_service.service.impl;

import com.ecommerce.account_service.dao.AccountRepository;
import com.ecommerce.account_service.entity.Account;
import com.ecommerce.account_service.exception.DuplicateResourceException;
import com.ecommerce.account_service.exception.ResourceNotFoundException;
import com.ecommerce.account_service.payload.AccountDTO;
import com.ecommerce.account_service.payload.BasicAccountDTO;
import com.ecommerce.account_service.service.AccountService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {

        if (accountRepository.existsByUserEmail(accountDTO.getUserEmail())) {
            throw new DuplicateResourceException("Account with email " + accountDTO.getUserEmail() + " already exists.");
        }
        if (accountRepository.existsByUserName(accountDTO.getUserName())) {
            throw new DuplicateResourceException("Account with username " + accountDTO.getUserName() + " already exists.");
        }

        Account account = new Account();
        account.setUserEmail(accountDTO.getUserEmail());
        account.setUserName(accountDTO.getUserName());
        account.setBillingAddress(accountDTO.getBillingAddress());
        account.setPaymentMethod(accountDTO.getPaymentMethod());

        Account savedAccount = accountRepository.save(account);
        return convertToAccountDTO(savedAccount);
    }

    @Override
    public List<BasicAccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::convertToBasicAccountDTO).collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(this::convertToAccountDTO).orElse(null);
    }

    @Override
    public AccountDTO updateAccount(Long id, AccountDTO accountDTO) {
        Optional<Account> existingAccount = accountRepository.findById(id);
        if (existingAccount.isPresent()) {
            Account account = existingAccount.get();
            account.setUserEmail(accountDTO.getUserEmail());
            account.setUserName(accountDTO.getUserName());
            account.setShippingAddress(accountDTO.getShippingAddress());
            account.setBillingAddress(accountDTO.getBillingAddress());
            account.setPaymentMethod(accountDTO.getPaymentMethod());

            Account updatedAccount = accountRepository.save(account);
            return convertToAccountDTO(updatedAccount);
        }
        return null;
    }

    @Override
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account with ID " + id + " not found.");
        }

        accountRepository.deleteById(id);
    }

    private AccountDTO convertToAccountDTO(Account account) {
        return new AccountDTO(
                account.getId(),
                account.getUserEmail(),
                account.getUserName(),
                account.getShippingAddress(),
                account.getBillingAddress(),
                account.getPaymentMethod()
        );
    }

    private BasicAccountDTO convertToBasicAccountDTO(Account account) {
//        BasicAccountDTO basicAccountDTO = new BasicAccountDTO();
//        basicAccountDTO.setId(account.getId());
//        basicAccountDTO.setUserEmail(account.getUserEmail());
//        basicAccountDTO.setUserName(account.getUserName());
//        return basicAccountDTO;
    // constructor for fewer method calls
        return new BasicAccountDTO(
                account.getId(),
                account.getUserEmail(),
                account.getUserName());
    }
}
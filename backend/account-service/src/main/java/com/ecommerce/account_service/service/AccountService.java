package com.ecommerce.account_service.service;

import com.ecommerce.account_service.payload.AccountDTO;
import com.ecommerce.account_service.payload.BasicAccountDTO;

import java.util.List;

public interface AccountService {
    AccountDTO createAccount(AccountDTO accountDTO);
    List<BasicAccountDTO> getAllAccounts();
    AccountDTO getAccountById(Long id);
    AccountDTO updateAccount(Long id, AccountDTO accountDTO);
    void deleteAccount(Long id);
}

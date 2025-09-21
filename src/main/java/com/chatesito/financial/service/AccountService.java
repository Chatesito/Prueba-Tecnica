package com.chatesito.financial.service;

import com.chatesito.financial.dto.account.AccountRequestDTO;
import com.chatesito.financial.dto.account.AccountResponseDTO;

import java.util.List;

public interface AccountService {

    AccountResponseDTO create(AccountRequestDTO request);
    AccountResponseDTO getById(Long id);
    List<AccountResponseDTO> getByClient(Long clientId);
    AccountResponseDTO update(Long id, AccountRequestDTO request);
    void changeStatus(Long id, String status);
    void delete(Long id);
}

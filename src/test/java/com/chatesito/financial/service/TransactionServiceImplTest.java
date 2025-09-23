package com.chatesito.financial.service;

import com.chatesito.financial.dto.transaction.TransactionRequestDTO;
import com.chatesito.financial.entity.Account;
import com.chatesito.financial.mapper.TransactionMapper;
import com.chatesito.financial.repository.AccountRepository;
import com.chatesito.financial.repository.TransactionRepository;
import com.chatesito.financial.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private TransactionMapper transactionMapper;
    private TransactionServiceImpl service;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        transactionMapper = mock(TransactionMapper.class);
        service = new TransactionServiceImpl(accountRepository, transactionRepository, transactionMapper);
    }

    @Test
    void transfer_shouldRejectSameAccount() {
        Account origin = new Account();
        origin.setStatus("Activa");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(origin));

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setTransactionType("Transferencia");
        dto.setAccountId(1L);
        dto.setTargetAccountId(1L);
        dto.setAmount(100.0);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("misma cuenta"));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void withdraw_shouldRejectInsufficientFunds() {
        Account origin = new Account();
        origin.setStatus("Activa");
        origin.setBalance(10.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(origin));

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setTransactionType("Retiro");
        dto.setAccountId(1L);
        dto.setAmount(100.0);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("fondos insuficientes"));
        verify(transactionRepository, never()).save(any());
    }
}

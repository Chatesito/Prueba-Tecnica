package com.chatesito.financial.service;

import com.chatesito.financial.dto.account.AccountRequestDTO;
import com.chatesito.financial.entity.Account;
import com.chatesito.financial.entity.Client;
import com.chatesito.financial.mapper.AccountMapper;
import com.chatesito.financial.repository.AccountRepository;
import com.chatesito.financial.repository.ClientRepository;
import com.chatesito.financial.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    private AccountRepository accountRepository;
    private ClientRepository clientRepository;
    private AccountMapper accountMapper;
    private AccountServiceImpl service;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        clientRepository = mock(ClientRepository.class);
        accountMapper = mock(AccountMapper.class);
        service = new AccountServiceImpl(accountRepository, clientRepository, accountMapper);
    }

    @Test
    void changeStatus_shouldRejectCancelWhenBalanceGreaterThanZero() {
        Account acc = new Account();
        acc.setBalance(100.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(acc));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.changeStatus(1L, "Cancelada"));
        assertTrue(ex.getMessage().toLowerCase().contains("saldo"));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void createSavings_shouldRejectNegativeInitialBalance() {
        AccountRequestDTO dto = new AccountRequestDTO();
        dto.setClientId(1L);
        dto.setAccountType("Ahorros");
        dto.setInitialBalance(-10.0);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(new Client()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("saldo negativo"));
        verify(accountRepository, never()).save(any());
    }
}

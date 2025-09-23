package com.chatesito.financial.service;

import com.chatesito.financial.dto.client.ClientRequestDTO;
import com.chatesito.financial.entity.Client;
import com.chatesito.financial.mapper.ClientMapper;
import com.chatesito.financial.repository.AccountRepository;
import com.chatesito.financial.repository.ClientRepository;
import com.chatesito.financial.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceImplTest {

    private ClientRepository clientRepository;
    private AccountRepository accountRepository;
    private ClientMapper clientMapper;
    private ClientServiceImpl service;

    @BeforeEach
    void setUp() {
        clientRepository = mock(ClientRepository.class);
        accountRepository = mock(AccountRepository.class);
        clientMapper = mock(ClientMapper.class);
        service = new ClientServiceImpl(clientRepository, accountRepository, clientMapper);
    }

    @Test
    void create_shouldRejectMinor() {
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setBirthDate(LocalDate.now().minusYears(10)); // -18 years

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        assertTrue(ex.getMessage().contains("mayor de edad"));
        verifyNoInteractions(clientRepository, clientMapper);
    }

    @Test
    void create_shouldRejectDuplicateEmail() {
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setBirthDate(LocalDate.now().minusYears(25));
        dto.setEmail("dup@example.com");
        dto.setIdentificationNumber("12345678901");
        when(clientRepository.existsByEmail("dup@example.com")).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> service.create(dto));
        verify(clientRepository, times(1)).existsByEmail("dup@example.com");
        verify(clientRepository, never()).save(any());
    }

    @Test
    void delete_shouldRejectWhenHasLinkedAccounts() {
        Long id = 7L;
        when(clientRepository.findById(id)).thenReturn(Optional.of(new Client()));
        when(accountRepository.existsByClientId(id)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.delete(id));
        assertTrue(ex.getMessage().contains("productos vinculados"));
        verify(clientRepository, never()).delete(any());
    }
}

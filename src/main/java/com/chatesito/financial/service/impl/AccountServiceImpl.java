package com.chatesito.financial.service.impl;

import com.chatesito.financial.dto.account.AccountRequestDTO;
import com.chatesito.financial.dto.account.AccountResponseDTO;
import com.chatesito.financial.entity.Account;
import com.chatesito.financial.entity.Client;
import com.chatesito.financial.mapper.AccountMapper;
import com.chatesito.financial.repository.AccountRepository;
import com.chatesito.financial.repository.ClientRepository;
import com.chatesito.financial.service.AccountService;
import com.chatesito.financial.util.AccountNumberGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private static final String TYPE_AHORROS = "AHORROS";
    private static final String TYPE_CORRIENTE = "CORRIENTE";
    private static final String STATUS_ACTIVA = "ACTIVA";
    private static final String STATUS_INACTIVA = "INACTIVA";
    private static final String STATUS_CANCELADA = "CANCELADA";

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository,
                              ClientRepository clientRepository,
                              AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional
    public AccountResponseDTO create(AccountRequestDTO request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado: " + request.getClientId()));

        String type = normalize(request.getAccountType());
        validateType(type);

        if (TYPE_AHORROS.equals(type) && request.getInitialBalance() != null && request.getInitialBalance() < 0) {
            throw new IllegalArgumentException("La cuenta de ahorros no puede tener saldo negativo");
        }

        Account entity = accountMapper.toEntity(request, client);

        // Normalize status, types and rules
        entity.setAccountType(capitalize(type));
        entity.setStatus(TYPE_AHORROS.equals(type) ? "Activa" : capitalize(normalize(request.getStatus())));

        // Prefix 53 for Ahorros, 33 for Corriente
        String prefix = TYPE_AHORROS.equals(type) ? "53" : "33";
        entity.setAccountNumber(generateUnique(prefix));

        // Align balances
        if (entity.getBalance() == null) entity.setBalance(0d);
        entity.setAvailableBalance(entity.getBalance());

        Account saved = accountRepository.save(entity);
        return accountMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDTO getById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + id));
        return accountMapper.toResponseDTO(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getByClient(Long clientId) {
        return accountMapper.toResponseDTO(accountRepository.findByClientId(clientId));
    }

    @Override
    @Transactional
    public AccountResponseDTO update(Long id, AccountRequestDTO request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + id));

        // Changing account number or balance via DTO update not allowed
        accountMapper.updateEntity(request, account, account.getClient());

        String type = normalize(account.getAccountType());
        validateType(type);
        if ("Ahorros".equalsIgnoreCase(account.getAccountType()) && (account.getBalance() != null && account.getBalance() < 0)) {
            throw new IllegalArgumentException("La cuenta de ahorros no puede tener saldo negativo");
        }
        Account saved = accountRepository.save(account);
        return accountMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void changeStatus(Long id, String statusInput) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + id));
        String status = normalize(statusInput);
        if (!status.equals(STATUS_ACTIVA) && !status.equals(STATUS_INACTIVA) && !status.equals(STATUS_CANCELADA)) {
            throw new IllegalArgumentException("Estado inválido. Use: Activa, Inactiva, Cancelada");
        }
        if (status.equals(STATUS_CANCELADA) && (account.getBalance() != null && account.getBalance() != 0d)) {
            throw new IllegalArgumentException("Solo se puede cancelar con saldo igual a 0");
        }
        account.setStatus(capitalize(status));
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + id));
        accountRepository.delete(account);
    }

    private String generateUnique(String prefix) {
        for (int i = 0; i < 25; i++) {
            String candidate = AccountNumberGenerator.generateWithPrefix(prefix);
            if (accountRepository.findByAccountNumber(candidate).isEmpty()) {
                return candidate;
            }
        }
        throw new DataIntegrityViolationException("No fue posible generar un número de cuenta único");
    }

    private void validateType(String type) {
        if (!TYPE_AHORROS.equals(type) && !TYPE_CORRIENTE.equals(type)) {
            throw new IllegalArgumentException("Tipo de cuenta inválido. Use: Corriente o Ahorros");
        }
    }

    private String normalize(String v) {
        return v == null ? "" : v.trim().toUpperCase();
    }

    private String capitalize(String v) {
        if (v == null || v.isBlank()) return v;
        String low = v.toLowerCase();
        return Character.toUpperCase(low.charAt(0)) + low.substring(1);
    }
}

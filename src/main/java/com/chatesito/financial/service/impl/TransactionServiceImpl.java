package com.chatesito.financial.service.impl;

import com.chatesito.financial.dto.transaction.TransactionRequestDTO;
import com.chatesito.financial.dto.transaction.TransactionResponseDTO;
import com.chatesito.financial.entity.Account;
import com.chatesito.financial.entity.Transaction;
import com.chatesito.financial.mapper.TransactionMapper;
import com.chatesito.financial.repository.AccountRepository;
import com.chatesito.financial.repository.TransactionRepository;
import com.chatesito.financial.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(AccountRepository accountRepository,
                                  TransactionRepository transactionRepository,
                                  TransactionMapper transactionMapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public TransactionResponseDTO create(TransactionRequestDTO request) {
        Account origin = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + request.getAccountId()));
        ensureActive(origin);

        String type = normalize(request.getTransactionType());

        switch (type) {
            case "CONSIGNACION" -> {
                double newBalance = (origin.getBalance() == null ? 0d : origin.getBalance()) + request.getAmount();
                origin.setBalance(newBalance);
                origin.setAvailableBalance(newBalance);
                accountRepository.save(origin);

                Transaction tx = transactionMapper.toEntity(request, origin, null);
                Transaction saved = transactionRepository.save(tx);
                return transactionMapper.toResponseDTO(saved);
            }
            case "RETIRO" -> {
                double current = origin.getBalance() == null ? 0d : origin.getBalance();
                if (current < request.getAmount()) {
                    throw new IllegalArgumentException("Fondos insuficientes para el retiro");
                }
                double newBalance = current - request.getAmount();
                if ("Ahorros".equalsIgnoreCase(origin.getAccountType()) && newBalance < 0) {
                    throw new IllegalArgumentException("La cuenta de ahorros no puede tener saldo negativo");
                }
                origin.setBalance(newBalance);
                origin.setAvailableBalance(newBalance);
                accountRepository.save(origin);

                Transaction tx = transactionMapper.toEntity(request, origin, null);
                Transaction saved = transactionRepository.save(tx);
                return transactionMapper.toResponseDTO(saved);
            }
            case "TRANSFERENCIA" -> {
                if (request.getTargetAccountId() == null) {
                    throw new IllegalArgumentException("El numero de cuenta de destino es requerido para Transferencia");
                }
                if (request.getTargetAccountId().equals(request.getAccountId())) {
                    throw new IllegalArgumentException("No puedes transferir a la misma cuenta");
                }
                Account target = accountRepository.findById(request.getTargetAccountId())
                        .orElseThrow(() -> new EntityNotFoundException("Cuenta destino no encontrada: " + request.getTargetAccountId()));
                ensureActive(target);

                double originBalance = origin.getBalance() == null ? 0d : origin.getBalance();
                if (originBalance < request.getAmount()) {
                    throw new IllegalArgumentException("Fondos insuficientes para la transferencia");
                }
                double targetBalance = target.getBalance() == null ? 0d : target.getBalance();

                // Debit origin
                origin.setBalance(originBalance - request.getAmount());
                origin.setAvailableBalance(origin.getBalance());

                // prove destination
                target.setBalance(targetBalance + request.getAmount());
                target.setAvailableBalance(target.getBalance());

                accountRepository.save(origin);
                accountRepository.save(target);

                // Create two transactions: debit (source) and credit (destination)
                List<Transaction> toSave = new ArrayList<>();
                Transaction debit = transactionMapper.toEntity(request, origin, target);
                toSave.add(debit);
                Transaction credit = transactionMapper.toEntity(request, target, origin);
                toSave.add(credit);

                List<Transaction> saved = transactionRepository.saveAll(toSave);
                return transactionMapper.toResponseDTO(saved.get(0));
            }
            default -> throw new IllegalArgumentException("Tipo de transacción no soportado: " + request.getTransactionType());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> listByAccount(Long accountId, LocalDateTime from, LocalDateTime to) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + accountId));
        List<Transaction> txs = (from != null && to != null)
                ? transactionRepository.findByAccountIdAndTransactionDateBetween(accountId, from, to)
                : transactionRepository.findByAccountId(accountId);
        return transactionMapper.toResponseDTO(txs);
    }

    private void ensureActive(Account account) {
        if (!"Activa".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalStateException("La cuenta no está activa");
        }
    }

    private String normalize(String input) {
        if (input == null) return "";
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toUpperCase();
    }
}

package com.chatesito.financial.repository;

import com.chatesito.financial.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByClientId(Long clientId);
    Optional<Account> findByAccountNumber(String accountNumber);

    // Dont delete client with products linked
    boolean existsByClientId(Long clientId);
}

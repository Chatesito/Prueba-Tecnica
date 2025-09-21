package com.chatesito.financial.repository;

import com.chatesito.financial.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}

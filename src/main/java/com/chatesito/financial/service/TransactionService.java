package com.chatesito.financial.service;

import com.chatesito.financial.dto.transaction.TransactionRequestDTO;
import com.chatesito.financial.dto.transaction.TransactionResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    TransactionResponseDTO create(TransactionRequestDTO request);
    List<TransactionResponseDTO> listByAccount(Long accountId, LocalDateTime from, LocalDateTime to);
}

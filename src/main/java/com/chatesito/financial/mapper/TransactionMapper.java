package com.chatesito.financial.mapper;

import com.chatesito.financial.dto.transaction.TransactionRequestDTO;
import com.chatesito.financial.dto.transaction.TransactionResponseDTO;
import com.chatesito.financial.entity.Account;
import com.chatesito.financial.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // Create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionDate", ignore = true) // it sets on @PrePersist
    @Mapping(target = "account", source = "account")
    @Mapping(target = "targetAccount", source = "targetAccount")
    Transaction toEntity(TransactionRequestDTO source, Account account, Account targetAccount);

    // Response
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "targetAccountId", source = "targetAccount.id")
    TransactionResponseDTO toResponseDTO(Transaction source);

    List<TransactionResponseDTO> toResponseDTO(List<Transaction> source);
}

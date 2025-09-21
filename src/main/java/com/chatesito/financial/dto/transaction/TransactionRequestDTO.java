package com.chatesito.financial.dto.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class TransactionRequestDTO {

    @NotNull
    @Pattern(regexp = "(?i)(Consignaci[oó]n|Retiro|Transferencia)", message = "El tipo de transacción debe ser Consignación, Retiro o Transferencia")
    private String transactionType;

    @NotNull
    @Positive
    private Double amount;

    @NotNull
    private Long accountId;

    private Long targetAccountId; // When transfer

    // Getters and Setters
    public String getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }
    public void setTargetAccountId(Long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }
}

package com.chatesito.financial.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class AccountRequestDTO {

    @NotNull
    private Long clientId;

    private String accountNumber;

    @NotBlank
    @Size(max = 20)
    private String accountType; // "Corriente", "Ahorros"

    @PositiveOrZero
    private Double initialBalance = 0.0;

    private String status; // "Activa", "Inactiva", "Cancelada"

    @NotNull
    private Boolean exemptGMF = Boolean.FALSE;

    // Getters and Setters
    public Long getClientId() {
        return clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getInitialBalance() {
        return initialBalance;
    }
    public void setInitialBalance(Double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getExemptGMF() {
        return exemptGMF;
    }
    public void setExemptGMF(Boolean exemptGMF) {
        this.exemptGMF = exemptGMF;
    }
}

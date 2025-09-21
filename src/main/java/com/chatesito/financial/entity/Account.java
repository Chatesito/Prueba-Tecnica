package com.chatesito.financial.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10, unique = true)
    private String accountNumber;

    @Column(nullable = false, length = 20)
    private String accountType; // "Corriente", "Ahorros"

    @Column(nullable = false, length = 20)
    private String status; // "Activa", "Inactiva", "Cancelada"

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false)
    private Boolean exemptGMF;

    @Column(nullable = false)
    private Double availableBalance;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column
    private LocalDate updatedAt;

    // Relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Account() {}

    // Constructors
    public Account(Long id, String accountNumber, String accountType, String status, Double balance, Boolean exemptGMF, LocalDate createdAt, LocalDate updatedAt, Client client) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.status = status;
        this.balance = balance;
        this.availableBalance = balance;
        this.exemptGMF = exemptGMF;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.client = client;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public Double getAvailableBalance() {
        return availableBalance;
    }
    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }
    public Boolean getExemptGMF() {
        return exemptGMF;
    }
    public void setExemptGMF(Boolean exemptGMF) {
        this.exemptGMF = exemptGMF;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDate getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    // Auto set dates
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}
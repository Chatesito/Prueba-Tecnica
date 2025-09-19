package com.chatesito.financial.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String transactionType; // "Consignación", "Retiro", "Transferencia"

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Relation when transfer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;

    // Constructors
    public Transaction() {}

    public Transaction(Long id, String transactionType, Double amount, LocalDateTime transactionDate, Account account, Account targetAccount) {
        this.id = id;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.account = account;
        this.targetAccount = targetAccount;
    }

    // Getters and Settters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public Account getTargetAccount() {
        return targetAccount;
    }
    public void setTargetAccount(Account targetAccount) {
        this.targetAccount = targetAccount;
    }

    // Auto set date
    @PrePersist
    public void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }
}

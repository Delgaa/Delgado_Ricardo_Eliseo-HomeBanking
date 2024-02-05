package com.mindhub.homebanking.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TransactionType transactionType;
    private LocalDateTime transactionTime;
    private Double amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction(TransactionType transactionType, LocalDateTime transactionTime, Double amount, Account account) {
        this.transactionType = transactionType;
        this.transactionTime = transactionTime;
        this.amount = amount;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", transactionType=" + transactionType +
                ", transactionTime=" + transactionTime +
                ", amount=" + amount +
                '}';
    }
}

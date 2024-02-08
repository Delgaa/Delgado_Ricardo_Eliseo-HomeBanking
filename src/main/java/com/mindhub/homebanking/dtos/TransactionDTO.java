package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;

    private TransactionType type;

    private String detail;

    private LocalDateTime date;

    private Double amount;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.detail = transaction.getDetail();
        this.date = transaction.getDate();
        this.amount = transaction.getAmount();
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }
}

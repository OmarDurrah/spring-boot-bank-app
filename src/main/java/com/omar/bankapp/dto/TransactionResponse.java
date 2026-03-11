package com.omar.bankapp.dto;

import java.time.LocalDateTime;
import com.omar.bankapp.entity.TransactionType;

public class TransactionResponse {

    private Long id;
    private Long accountId;
    private TransactionType type;
    private double amount;
    private LocalDateTime createdAt;

    public TransactionResponse(Long id,
                               Long accountId,
                               TransactionType type,
                               double amount,
                               LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
package com.omar.bankapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WithdrawRequest {

    @NotNull(message = "AccountId is required")
    @Positive(message = "AccountId must be positive")
    private Long accountId;

    @Positive(message = "Amount must be positive")
    private double amount;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
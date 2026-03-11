package com.omar.bankapp.dto;

import jakarta.validation.constraints.*;

public class TransferRequest {

    @Positive(message = "the first account must be positive")
    private Long fromAccountId;
    @Positive(message = "the second account  must be positive")
    private Long toAccountId;
    @Positive(message = "amount must be positive")
    private double amount;

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public double getAmount() {
        return amount;
    }
}
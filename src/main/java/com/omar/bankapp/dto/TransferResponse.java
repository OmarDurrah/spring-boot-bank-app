package com.omar.bankapp.dto;

public class TransferResponse {

    private Long fromAccountId;
    private Long toAccountId;
    private double amount;

    public TransferResponse(Long fromAccountId, Long toAccountId, double amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

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